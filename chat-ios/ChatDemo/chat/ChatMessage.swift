//
//  ChatMessage.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

// 信息类别
enum ChatType:Int{
    case HEARTBEAT = -1
    case LOGIN
    case SINGLE
    case GROUP
    case LOGIN_REPLY
}

// 信息内容格式
enum MsgType{
    case TEXT
    case IMG
    case VIDEO
    case AUDIO
    case HYBRID
    
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
        case .HYBRID:
            return 5;
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
        case .HYBRID:
            return "hybrid";
        }
    }
}

struct ChatMessage<T:Codable>: CGYJSON,Codable {
    var fromUserId:String!
    var toUserId:String!
    var id:String!
    var type:Int!
    var body:T?
}

class LoginBody:NSObject,CGYJSON,Codable {
    var userId:String!
    var userName:String!
    var login:Bool!
    
    override init(){
        
    }
}

class AbsBody:NSObject,CGYJSON,Codable {
    var messageBodyType:Int!
}

class TextBody: AbsBody{
    var text:String!
    
    private enum CodingKeys:String,CodingKey{
        case text
    }
    
    public override init(){
        super.init()
        self.messageBodyType = MsgType.TEXT.code
    }
    
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        try super.init(from: decoder)
        text = try container.decode(String.self, forKey: .text)
    }
    
}

