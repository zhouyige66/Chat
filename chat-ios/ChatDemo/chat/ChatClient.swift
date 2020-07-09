//
//  ChatClient.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation
import SwiftyJSON

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
        // 登录
        login(true)
        
        sock.readData(withTimeout: -1, tag: 0)
        startHeartbeat()
    }
    
    func socketDidDisconnect(_ sock: GCDAsyncSocket, withError err: Error?) {
        print("socket连接断开：\(err.debugDescription)")
        connectServerSuccess = false
    }
    
    func socket(_ sock: GCDAsyncSocket, didRead data: Data, withTag tag: Int) {
        sock.readData(withTimeout: -1, tag: 0)
        // 重置计数器
        timeCount = 0
        heartBeatFail = 0
        var jsonData:Data
        
        // 按编码类型解码数据
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

        // 按消息类型解析具体消息
        do{
            let json = try JSON(data: jsonData,options: [])
            let dic = json.dictionaryObject!
            let messagetTypeStr:String = dic["messageType"] as! String
            let messagetType:MessageType = MessageType(rawValue: messagetTypeStr)!
            if(messagetType != .HEARTBEAT){
                print("SwiftyJson解析数据\(json)")
            }
            
            let decoder = JSONDecoder()
            switch (messagetType){
            case MessageType.HEARTBEAT:
                // 消息消息，暂不处理
                break
            case MessageType.NOTIFY:
                let notifyMessage:NotifyMessage = try! decoder.decode(NotifyMessage.self, from: jsonData)
                guard let notifyDataStr:String = notifyMessage.data else {
                    print("通知消息无数据")
                    return
                }
                let notifyData = notifyDataStr.data(using: .utf8)!
                switch (notifyMessage.notifyMessageType) {
                case .LOGIN_REPLY:
                    let onlineIds:[Int64] = try! JSONSerialization.jsonObject(with: notifyData, options: .mutableContainers) as! [Int64]
                    print("在线名单\(onlineIds)")
                    ChatUserManager.shared.onlineUserIdSet = ChatUserManager.shared.onlineUserIdSet.union(onlineIds)
                    break
                case .LOGIN_NOTIFY:
                    let dict:Dictionary<String,Any> = try! JSONSerialization.jsonObject(with: notifyData, options: .mutableContainers) as! Dictionary
                    print("登录通知\(dict)")
                    let userId:Int64 = dict["id"] as! Int64
                    if(dict["login"] as! Bool == true){
                        ChatUserManager.shared.onlineUserIdSet.remove(userId)
                    }else{
                        ChatUserManager.shared.onlineUserIdSet.insert(userId)
                    }
                    break
                case .CHAT_MESSAGE_ID:
                    break
                case .IMPORTANT:
                    break
                default:
                    break
                }
                break
            case MessageType.CHAT:
                let chatMessage:ChatMessage = try! decoder.decode(ChatMessage.self, from: jsonData)
                print("收到聊天消息\(chatMessage)")
                // 更新最近联系人
                ChatUserManager.shared.addContact(chatMessage: chatMessage,receive: true)
                // 存储聊天消息
                MessageManager.shared.cache(chatMessage)
                break
            default:
                break
            }
        }catch{
            print("解析异常")
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
            login(false)
            // 断开连接
            clientSocket.disconnect()
            clientSocket.delegate = nil
            clientSocket.delegateQueue = nil
        }
    }
    
    // 发送消息
    public func send(_ message:Message){
        if message.messageType == MessageType.CHAT {
            let chatMessage = message as! ChatMessage
            ChatUserManager.shared.addContact(chatMessage: chatMessage, receive: false)
            MessageManager.shared.cache(chatMessage)
        }
        
        if(clientSocket.isConnected){
            var jsonStr = message.toJSONString()
            var data:Data = Data()
            
            switch coderType {
            case .STRING:// 方式一：原字符串
                // 使用
                data = jsonStr!.data(using: String.Encoding.utf8)!
                break
            case .DELIMITER://方式二：添加分隔符
                jsonStr!.append(DELIMITER)
                if(message.messageType != MessageType.HEARTBEAT){
                    print("发送消息：\(message.toJSONString()!)")
                }
                data = jsonStr!.data(using: String.Encoding.utf8)!
            case .CUSTOM:// 方式三：添加头部标志和长度
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
                break
            }
            clientSocket.write(data, withTimeout: 10000, tag: 1)
        }else{
            print("未连接服务器，请先连接服务器")
        }
    }
    
    /**以下是非公开方法**/
    private func login(_ isLogin:Bool){
        let loginMessage:LoginMessage = LoginMessage()
        loginMessage.clientType = ClientType.IOS
        loginMessage.userId = CacheManager.shared.getUserId()
        loginMessage.userName = CacheManager.shared.getUserName()
        loginMessage.device = UUID.init().uuidString
        loginMessage.location = "暂无"
        loginMessage.login = isLogin
        send(loginMessage)
    }
    
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
                // 心跳间隔5S
                sleep(5)
            }
        }
    }
    
}
