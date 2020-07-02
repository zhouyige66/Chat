//
//  ChatMessage.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

// 消息类型
enum MessageType:String,Codable {
    case HEARTBEAT,LOGIN,NOTIFY,APPLY,CHAT,FORWARD
    
    var code:Int{
        switch self {
        case .HEARTBEAT:
            return 0;
        case .LOGIN:
            return 1;
        case .NOTIFY:
            return 2;
        case .APPLY:
            return 3;
        case .CHAT:
            return 4;
        case .FORWARD:
            return 4;
        }
    }
    
    var des:String{
        switch self {
        case .HEARTBEAT:
            return "心跳";
        case .LOGIN:
            return "登录";
        case .NOTIFY:
            return "通知";
        case .APPLY:
            return "申请";
        case .CHAT:
            return "聊天";
        case .FORWARD:
            return "转发";
        }
    }
}

// 消息基类
class Message: NSObject,Codable {
    var messageType: MessageType!
}

// 登录客户端类型
enum ClientType:String,Codable {
    case ANDROID,IOS,PC,WEB
    
    var code:Int{
        switch self {
        case .ANDROID:
            return 0;
        case .IOS:
            return 1;
        case .PC:
            return 2;
        case .WEB:
            return 3;
        }
    }
    
    var des:String{
        switch self {
        case .ANDROID:
            return "android";
        case .IOS:
            return "ios";
        case .PC:
            return "pc";
        case .WEB:
            return "web";
        }
    }
}

// 登录消息
class LoginMessage:Message{
    var clientType:ClientType!
    var userId:Int64!
    var userName:String!
    var device:String!
    var location:String?
    var login:Bool!
    
    override init() {
        super.init()
        self.messageType = MessageType.LOGIN
    }
    
    required init(from decoder: Decoder) throws {
        fatalError("init(from:) has not been implemented")
    }
}

// 心跳消息
class HeartbeatMessage: Message {
    var data:String?
    
    override init() {
        super.init()
        self.messageType = MessageType.HEARTBEAT
    }
    
    required init(from decoder: Decoder) throws {
        fatalError("init(from:) has not been implemented")
    }
}

// 通知消息类型
enum NotifyMessageType {
    case LOGIN_REPLY      // 登录回复
    case LOGIN_NOTIFY     // 登录通知
    case CHAT_MESSAGE_ID  // 消息ID通知
    case IMPORTANT        // 重要通知
}

class NotifyMessage:Message{
    var notifyMessageType:NotifyMessageType!
    var data:String?
    
    override init() {
        super.init()
        self.messageType = MessageType.NOTIFY
    }
    
    required init(from decoder: Decoder) throws {
        fatalError("init(from:) has not been implemented")
    }
}

// 聊天信息类别
enum ChatMessageType:String,Codable{
    case SINGLE
    case GROUP
}

// 聊天信息格式
enum BodyType:String,Codable{
    case TEXT,IMG,VIDEO,AUDIO,FILE,LINK,HYBRID
    
    var code:Int{
        switch self {
        case .TEXT:
            return 1;
        case .IMG:
            return 2;
        case .VIDEO:
            return 3;
        case .AUDIO:
            return 4;
        case .FILE:
            return 5;
        case .LINK:
            return 6;
        case .HYBRID:
            return 7;
        }
    }
    
    var des:String{
        switch self {
        case .TEXT:
            return "text";
        case .IMG:
            return "img";
        case .VIDEO:
            return "video";
        case .AUDIO:
            return "audio";
        case .FILE:
            return "file";
        case .LINK:
            return "link";
        case .HYBRID:
            return "hybrid";
        }
    }
}

class ChatMessage<T:Codable>:Message {
    var chatMessageType:ChatMessageType!
    var id:String!
    var fromUserId:String!
    var toUserId:String!
    var bodyType:BodyType!
    var body:String!
    var sendTimestamp:Data!
    
    override init() {
        super.init()
        self.messageType = MessageType.CHAT
    }
    
    required init(from decoder: Decoder) throws {
        fatalError("init(from:) has not been implemented")
    }
    
    func setBody(bodyData:BodyData){
        bodyType = bodyData.bodyType
        body = bodyData.toJSONString()
    }
}

class BodyData:NSObject,CGYJSON,Codable {
    var bodyType:BodyType!
}

class TextBody: BodyData{
    var text:String!
    
    private enum CodingKeys:String,CodingKey{
        case text
    }
    
    public override init(){
        super.init()
        self.bodyType = BodyType.TEXT
    }
    
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        
        try super.init(from: decoder)
        text = try container.decode(String.self, forKey: .text)
    }
    
}

