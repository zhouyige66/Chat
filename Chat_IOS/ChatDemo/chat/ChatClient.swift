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
    
    // socket
    var clientSocket:GCDAsyncSocket!
    // 心跳计数参数
    var heartBeatFail = 0
    var timeCount = 0
    // 消息分隔符
    let DELIMITER:String = "*&v_secretary&*"
    // 消息头标识
    let HEAD_DATA:UInt32 = 0x8888
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
        var loginMessage:ChatMessage = ChatMessage<LoginBody>()
        loginMessage.fromUserId = CacheManager.shared.getUserId()
        loginMessage.toUserId = "server"
        loginMessage.id = UUID.init().uuidString
        loginMessage.type = ChatType.LOGIN.rawValue
        let loginBody:LoginBody = LoginBody()
        loginBody.userId = CacheManager.shared.getUserId()
        loginBody.userName = CacheManager.shared.getUserName()
        loginBody.login = true
        loginMessage.body = loginBody
        send(loginMessage)
        
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
        // 方式一：原字符串
//        jsonData = data
        // 方式二：分隔符
//        guard let msg = String.init(data: data, encoding: String.Encoding.utf8) else {
//            print("读取消息为空！")
//            return
//        }
//        let index = msg.range(of: DELIMITER)
//        jsonData = msg[msg.startIndex ..< index!.lowerBound].data(using: String.Encoding.utf8)!
        // 方式三：包含头部标志和长度
        // 1.读取头部信息
        var range:Range<Data.Index> = data.startIndex ..< data.startIndex.advanced(by: 4)
        let headData = data.subdata(in: range)
        let head = CommonUtil.data2Int(originData: headData)
//        let head1 = UInt32(headData[0]) << 24
//        let head2 = UInt32(headData[1]) << 16
//        let head3 = UInt32(headData[2]) << 8
//        let head4 = UInt32(headData[3])
//        let head = head1 + head2 + head3 + head4
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
        
        let json:Dictionary<String,Any> = try! JSONSerialization.jsonObject(with: jsonData, options: []) as! Dictionary<String, Any>
        let chatType:Int = json["type"] as! Int
        if(chatType != ChatType.HEARTBEAT.rawValue){
            print("收到消息：\(json)")
        }
        let decoder = JSONDecoder()
        switch (chatType){
        case ChatType.HEARTBEAT.rawValue:
            // 消息消息，暂不处理
            break
        case ChatType.LOGIN.rawValue:
            let loginMessage =  try! decoder.decode(ChatMessage<LoginBody>.self, from: jsonData)
            let loginBody = loginMessage.body!
            let user:User = User()
            user.id = loginBody.userId
            user.name = loginBody.userName
            if(loginBody.login){
                ChatUserManager.shared.addUser(user)
            }else{
                ChatUserManager.shared.removeUser(user)
            }
            break
        case ChatType.SINGLE.rawValue:
//            let fromUserId:String = json["fromUserId"] as! String
//            let toUserId:String = json["toUserId"] as! String
//            let body:Dictionary<String,Any> = json["body"] as! Dictionary<String, Any>
            MessageManager.shared.store(chatMessage: json)
            break
        case ChatType.GROUP.rawValue:
            break
        case ChatType.LOGIN_REPLY.rawValue:
            let userList:Array<Dictionary<String,Any>> = json["body"] as! Array<Dictionary<String,Any>>
            for user in userList{
                let userId:String = user["id"] as! String
                if(userId == CacheManager.shared.getUserId()){
                    continue
                }
                let u:User = User()
                u.id = userId
                u.name = user["name"] as! String
                ChatUserManager.shared.addUser(u)
            }
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
            try clientSocket.connect(toHost: "127.0.0.1", onPort: 9999)
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
            var logoutMessage:ChatMessage = ChatMessage<LoginBody>()
            logoutMessage.fromUserId = CacheManager.shared.getUserId()
            logoutMessage.toUserId = "server"
            logoutMessage.id = UUID.init().uuidString
            logoutMessage.type = ChatType.LOGIN.rawValue
            let loginBody:LoginBody = LoginBody()
            loginBody.userId = CacheManager.shared.getUserId()
            loginBody.userName = CacheManager.shared.getUserName()
            loginBody.login = false
            logoutMessage.body = loginBody
            send(logoutMessage)
            
            clientSocket.disconnect()
            clientSocket.delegate = nil
            clientSocket.delegateQueue = nil
        }
    }
    
    // 发送消息
    public func send<T>(_ message:ChatMessage<T>){
        if(clientSocket.isConnected){
            var data:Data = Data()
            // 方式一：原字符串
//            let msgStr = message.toJSONString()!
//            data = msgStr.data(using: String.Encoding.utf8)!
            // 方式二：添加分隔符
//            var msgStr = message.toJSONString()!
//            msgStr.append(DELIMITER)
//            data = msgStr.data(using: String.Encoding.utf8)!
            // 方式三：添加头部标志和长度
            // 1.写入头部标志
            let headBytes:[UInt8] = CommonUtil.int2Bytes(originValue: HEAD_DATA)
            data.append(contentsOf: headBytes)
            // 2.写入长度
            let msgStr = message.toJSONString()!
            let chatData:Data = msgStr.data(using: String.Encoding.utf8)!
            let length = UInt32(chatData.count)
            let lengthBytes:[UInt8] = CommonUtil.int2Bytes(originValue: length)
            data.append(contentsOf: lengthBytes)
            // 3.写入真正数据
            data.append(chatData)
            
            clientSocket.write(data, withTimeout: 10000, tag: 1)
        }else{
            print("未连接服务器，请先连接服务器")
        }
    }
   
    // 开启心跳检测
    private func startHeartbeat(){
        DispatchQueue.global().sync {
            while(connectServerSuccess){
                timeCount = timeCount + 1
                if(timeCount > 4){
                    // 发送心跳包
                    var heartbeatMessage:ChatMessage = ChatMessage<String>()
                    heartbeatMessage.fromUserId = CacheManager.shared.getUserId()
                    heartbeatMessage.toUserId = "server"
                    heartbeatMessage.id = UUID.init().uuidString
                    heartbeatMessage.type = ChatType.HEARTBEAT.rawValue
                    heartbeatMessage.body = ""
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
