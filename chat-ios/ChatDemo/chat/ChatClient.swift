//
//  ChatClient.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

final class ChatClient: NSObject,GCDAsyncSocketDelegate{
    static let shared = ChatClient()
    
    enum CoderType {
        case STRING,DELIMITER,CUSTOM
    }
    
    // socket
    var clientSocket:GCDAsyncSocket!
    // 心跳计数参数
    var heartBeatFail = 0
    var timeCount = 0
    // 消息分隔符
    let DELIMITER:String = "*&v_secretary&*"
    // 消息头标识
    let HEAD_DATA:UInt32 = 0x8888
    // 编码类型
    let coderType = CoderType.DELIMITER
    // 可观察属性
    @objc dynamic var connectServerSuccess = false
    
    // 私有化构造方法
    private override init() {
        clientSocket = GCDAsyncSocket()
    }
    
    /**以下是协议的代理方法**/
    func socket(_ sock: GCDAsyncSocket, didConnectToHost host: String, port: UInt16) {
        print("socket连接成功")
        // 发送登录消息
        let loginMessage:LoginMessage = LoginMessage()
        loginMessage.clientType = ClientType.IOS
        loginMessage.userId = CacheManager.shared.getUserId()
        loginMessage.userName = CacheManager.shared.getUserName()
        loginMessage.device = UUID.init().uuidString
        loginMessage.location = "暂无"
        loginMessage.login = true
        send(loginMessage)
        let jsonEncoder = JSONEncoder()
        let jsonData:Data = try!jsonEncoder.encode(loginMessage)
        let json = try!JSONSerialization.jsonObject(with: jsonData, options: .mutableContainers)
        var msgStr = String(data:jsonData,encoding:.utf8)!
        msgStr.append(DELIMITER)
        print(json)
        print("发送消息：" + msgStr)
    
        sock.readData(withTimeout: -1, tag: 0)
        startHeartbeat()
    }
    
    func socketDidDisconnect(_ sock: GCDAsyncSocket, withError err: Error?) {
        print("socket连接断开：\(err.debugDescription)")
        
        ChatUserManager.shared.clear()
        connectServerSuccess = false
    }
    
    func socket(_ sock: GCDAsyncSocket, didRead data: Data, withTag tag: Int) {
        sock.readData(withTimeout: -1, tag: 0)
        // 重置计数器
        timeCount = 0
        heartBeatFail = 0
        var jsonData:Data
        
        switch coderType {
        case .STRING:// 方式一：原字符串
            jsonData = data
            break;
        case .DELIMITER:// 方式二：分隔符
            guard let msg = String.init(data: data, encoding: String.Encoding.utf8) else {
                print("读取消息为空！")
                return
            }
            let index = msg.range(of: DELIMITER)
            jsonData = msg[msg.startIndex ..< index!.lowerBound].data(using: String.Encoding.utf8)!
            break;
        case .CUSTOM:// 方式三：包含头部标志和长度
            // 1.读取头部信息
            var range:Range<Data.Index> = data.startIndex ..< data.startIndex.advanced(by: 4)
            let headData = data.subdata(in: range)
            let head = CommonUtil.data2Int(originData: headData)
            // 判断消息是否与头部标志一致
            if(head != HEAD_DATA){
                print("该消息来源不对")
                return
            }
            // 2.读取数据长度
            range = data.startIndex.advanced(by: 4) ..< data.startIndex.advanced(by: 8)
            let lengthData = data.subdata(in: range)
            var length = CommonUtil.data2Int(originData: lengthData)
            length = length + 8
            // 3.读取信息数据
            jsonData = data.subdata(in: data.startIndex.advanced(by: 8) ..< data.startIndex.advanced(by: Int(length)))
            break
        }
        // 解析消息
        let json:Dictionary<String,Any> = try! JSONSerialization.jsonObject(with: jsonData, options: []) as! Dictionary<String, Any>
        let chatType:String = json["messageType"] as! String
        if(chatType != MessageType.HEARTBEAT.rawValue){
            print("收到消息：\(json)")
            return
        }
        
        let decoder = JSONDecoder()
        switch (chatType){
        case MessageType.HEARTBEAT.rawValue:
            // 消息消息，暂不处理
            break
        case MessageType.NOTIFY.rawValue:
            let notifyMessage:NotifyMessage = try! decoder.decode(NotifyMessage.self, from: jsonData)
            guard let notifyData:String = notifyMessage.data else {
                print("通知消息无数据")
                return
            }
            
            switch (notifyMessage.notifyMessageType) {
            case .LOGIN_REPLY:
                let onlineIds:[Int64] = try! JSONSerialization.jsonObject(with: data, options: .mutableContainers) as! [Int64]
                print(onlineIds)
                break
            case .LOGIN_NOTIFY:
                let data = notifyData.data(using: .utf8)!
                let dict:Dictionary<String,Any> = try! JSONSerialization.jsonObject(with: data, options: .mutableContainers) as! Dictionary
                print(dict)
                break
            case .CHAT_MESSAGE_ID:
                break
            case .IMPORTANT:
                break
            default:
                break
            }
            
            //            let loginMessage =
            //            let loginBody = loginMessage.body!
            //            let user:User = User()
            //            user.id = loginBody.userId
            //            user.name = loginBody.userName
            //            if(loginBody.login){
            //                ChatUserManager.shared.addUser(user)
            //            }else{
            //                ChatUserManager.shared.removeUser(user)
            //            }
            break
        case MessageType.CHAT.rawValue:
            //            let fromUserId:String = json["fromUserId"] as! String
            //            let toUserId:String = json["toUserId"] as! String
            //            let body:Dictionary<String,Any> = json["body"] as! Dictionary<String, Any>
            
            //            let userList:Array<Dictionary<String,Any>> = json["body"] as! Array<Dictionary<String,Any>>
            //            for user in userList{
            //                let userId:String = user["id"] as! String
            //                if(userId == CacheManager.shared.getUserId()){
            //                    continue
            //                }
            //                let u:User = User()
            //                u.id = userId
            //                u.name = user["name"] as! String
            //                ChatUserManager.shared.addUser(u)
            //            }
            //            MessageManager.shared.store(chatMessage: json)
            break
        default:
            break
        }
    }
    
    /**以下是对外公开方法**/
    
    // 打开socket连接
    public func connectServer(){
        clientSocket.delegate = self
        clientSocket.delegateQueue = DispatchQueue.global()
        do{
            try clientSocket.connect(toHost: "127.0.0.1", onPort: 20001)
            connectServerSuccess = true
        }catch{
            print("connect error")
            connectServerSuccess = false
        }
    }
    
    // 断开socket连接
    public func disConnectServer(){
        connectServerSuccess = false
        if(clientSocket.isConnected){
            // 发送下线信息
            //            var logoutMessage:ChatMessage = ChatMessage<LoginBody>()
            //            logoutMessage.fromUserId = CacheManager.shared.getUserId()
            //            logoutMessage.toUserId = "server"
            //            logoutMessage.id = UUID.init().uuidString
            //            logoutMessage.type = ChatType.LOGIN.rawValue
            //            let loginBody:LoginBody = LoginBody()
            //            loginBody.userId = CacheManager.shared.getUserId()
            //            loginBody.userName = CacheManager.shared.getUserName()
            //            loginBody.login = false
            //            logoutMessage.body = loginBody
            //            send(logoutMessage)
            
            clientSocket.disconnect()
            clientSocket.delegate = nil
            clientSocket.delegateQueue = nil
        }
    }
    
    // 发送消息
    public func send(_ message:Message){
        if(clientSocket.isConnected){
            var data:Data = Data()
            
            switch coderType {
            case .STRING:// 方式一：原字符串
                let jsonEncoder = JSONEncoder()
                data = try!jsonEncoder.encode(message)
                break
            case .DELIMITER://方式二：添加分隔符
                let jsonEncoder = JSONEncoder()
                let jsonData:Data = try!jsonEncoder.encode(message)
                let json = try!JSONSerialization.jsonObject(with: jsonData, options: .mutableContainers)
                var msgStr = String(data:jsonData,encoding:.utf8)!
                msgStr.append(DELIMITER)
                if(message.messageType != MessageType.HEARTBEAT){
                    print(json)
                    print("发送消息：" + msgStr)
                }
                data = msgStr.data(using: String.Encoding.utf8)!
            case .CUSTOM:// 方式三：添加头部标志和长度
//                // 1.写入头部标志
//                let headBytes:[UInt8] = CommonUtil.int2Bytes(originValue: HEAD_DATA)
//                data.append(contentsOf: headBytes)
//                // 2.写入长度
//                let msgStr = message.toJSONString()!
//                let chatData:Data = msgStr.data(using: String.Encoding.utf8)!
//                let length = UInt32(chatData.count)
//                let lengthBytes:[UInt8] = CommonUtil.int2Bytes(originValue: length)
//                data.append(contentsOf: lengthBytes)
//                // 3.写入真正数据
//                data.append(chatData)
                break
            }
            clientSocket.write(data, withTimeout: 10000, tag: 1)
        }else{
            print("未连接服务器，请先连接服务器")
        }
    }
    
    /**以下是非公开方法**/
    // 开启心跳检测
    private func startHeartbeat(){
        DispatchQueue.global().sync {
            while(connectServerSuccess){
                timeCount = timeCount + 1
                if(timeCount > 4){
                    // 发送心跳包
                    let heartbeatMessage:HeartbeatMessage = HeartbeatMessage()
                    send(heartbeatMessage)
                    
                    heartBeatFail = heartBeatFail + 1
                    if(heartBeatFail > 2){
                        disConnectServer()
                    }
                }
                sleep(1)
            }
        }
    }
    
}
