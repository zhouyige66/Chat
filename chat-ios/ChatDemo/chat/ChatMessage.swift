//
//  ChatMessage.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

// 消息类型
enum MessageType:String,Codable,CGYJSON {
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
    
    func toJSONModel() -> AnyObject? {
        return self.rawValue as AnyObject
    }
}

// 登录客户端类型
enum ClientType:String,Codable,CGYJSON {
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
    
    func toJSONModel() -> AnyObject? {
        return self.rawValue as AnyObject
    }
}

// 通知消息类型
enum NotifyMessageType:String,Codable,CGYJSON{
    case LOGIN_REPLY      // 登录回复
    case LOGIN_NOTIFY     // 登录通知
    case CHAT_MESSAGE_ID  // 消息ID通知
    case IMPORTANT        // 重要通知
    
    func toJSONModel() -> AnyObject? {
        return self.rawValue as AnyObject
    }
}

// 聊天信息类别
enum ChatMessageType:String,Codable,CGYJSON{
    case SINGLE
    case GROUP
    
    func toJSONModel() -> AnyObject? {
        return self.rawValue as AnyObject
    }
}

// 聊天信息格式
enum BodyType:String,Codable,CGYJSON{
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
    
    func toJSONModel() -> AnyObject? {
        return self.rawValue as AnyObject
    }
}

// 消息基类
class Message: NSObject,Codable,CGYJSON {
    var messageType: MessageType!
}

// 登录消息
class LoginMessage:Message{
    var clientType:ClientType!
    var userId:Int64!
    var userName:String!
    var device:String!
    var location:String?
    var login:Bool!
    
    private enum CodingKeys:String, CodingKey {
        // case messageType = "messageType"
        case clientType = "clientType"
        case userId = "userId"
        case userName = "userName"
        case device = "device"
        case location = "location"
        case login = "login"
    }
    
    override init() {
        super.init()
        self.messageType = MessageType.LOGIN
    }
    
    // 如果要 JSON -> Model 必须实现这个方法
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        clientType = try container.decode(ClientType.self, forKey: .clientType)
        userId = try container.decode(Int64.self, forKey: .userId)
        userName = try container.decode(String.self, forKey: .userName)
        device = try container.decode(String.self, forKey: .device)
        location = try container.decode(String.self, forKey: .location)
        login = try container.decode(Bool.self, forKey: .login)
        try super.init(from: decoder)
    }
    // 如果要 Model -> JSON 必须实现这个方法
    override func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(clientType, forKey: .clientType)
        try container.encode(userId, forKey: .userId)
        try container.encode(userName, forKey: .userName)
        try container.encode(device, forKey: .device)
        try container.encode(location, forKey: .location)
        try container.encode(login, forKey: .login)
        try super.encode(to: encoder)
    }
}

// 心跳消息
class HeartbeatMessage: Message {
    var data:String?
    
    private enum CodingKeys:String, CodingKey {
        case data = "data"
    }
    
    override init() {
        super.init()
        self.messageType = MessageType.HEARTBEAT
    }
    
    // 如果要 JSON -> Model 必须实现这个方法
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        data = try container.decode(String.self, forKey: .data)
        try super.init(from: decoder)
    }
    // 如果要 Model -> JSON 必须实现这个方法
    override func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(data, forKey: .data)
        try super.encode(to: encoder)
    }
}

// 通知消息
class NotifyMessage:Message{
    var notifyMessageType:NotifyMessageType!
    var data:String?
    
    private enum CodingKeys:String, CodingKey {
        case notifyMessageType = "notifyMessageType"
        case data = "data"
    }
    
    override init() {
        super.init()
        self.messageType = MessageType.LOGIN
    }
    
    // 如果要 JSON -> Model 必须实现这个方法
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        notifyMessageType = try container.decode(NotifyMessageType.self, forKey: .notifyMessageType)
        data = try container.decode(String.self, forKey: .data)
        try super.init(from: decoder)
    }
    // 如果要 Model -> JSON 必须实现这个方法
    override func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(notifyMessageType, forKey: .notifyMessageType)
        try container.encode(data, forKey: .data)
        try super.encode(to: encoder)
    }
}

// 聊天消息
class ChatMessage:Message{
    var chatMessageType:ChatMessageType!
    var id:Int64!
    var fromUserId:Int64!
    var toUserId:Int64!
    var bodyType:BodyType!
    var body:String!
    var sendTimestamp:Date!
    
    private enum CodingKeys:String, CodingKey {
        case chatMessageType = "chatMessageType"
        case id = "id"
        case fromUserId = "fromUserId"
        case toUserId = "toUserId"
        case bodyType = "bodyType"
        case body = "body"
        case sendTimestamp = "sendTimestamp"
    }
    
    override init() {
        super.init()
        self.messageType = MessageType.CHAT
    }
    
    // 如果要 JSON -> Model 必须实现这个方法
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        chatMessageType = try container.decode(ChatMessageType.self,forKey: .chatMessageType)
        id = try container.decode(Int64.self,forKey: .id)
        fromUserId = try container.decode(Int64.self,forKey: .fromUserId)
        toUserId = try container.decode(Int64.self,forKey: .toUserId)
        bodyType = try container.decode(BodyType.self,forKey: .bodyType)
        body = try container.decode( String.self,forKey: .body)
        sendTimestamp = try container.decode(Date.self,forKey: .sendTimestamp)
        
        try super.init(from: decoder)
    }
    // 如果要 Model -> JSON 必须实现这个方法
    override func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(chatMessageType,forKey: .chatMessageType)
        try container.encode(id ,forKey: .id)
        try container.encode(fromUserId ,forKey: .fromUserId)
        try container.encode(toUserId ,forKey: .toUserId)
        try container.encode(bodyType,forKey: .bodyType)
        try container.encode(body ,forKey: .body)
        try container.encode(sendTimestamp,forKey: .sendTimestamp)
        try super.encode(to: encoder)
    }
    
    func setBody(bodyData:BodyData){
        bodyType = bodyData.bodyType
        body = bodyData.toJSONString()
    }
    
    func getMsgTime() -> String {
        guard sendTimestamp != nil else {
            return "解析错误"
        }
        return sendTimestamp.toJSONModel()! as! String
    }
    
    func getMessageValue()->String{
        var msg = ""
        switch bodyType {
        case .TEXT:
            let textBody = JsonUtil.json2Model(body, TextBody.self)
            msg = textBody!.text
            break
        default:
            msg = body
            break
        }
        return msg
    }
}

// 聊天消息体
class BodyData:NSObject,CGYJSON,Codable {
    var bodyType:BodyType!
}

// 聊天消息体-》文本t消息
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

