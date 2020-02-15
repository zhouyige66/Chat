//
//  MessageManager.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/11.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

final class MessageManager:NSObject {
    static let shared = MessageManager()

    @objc dynamic var messageDic:Dictionary<String, Array<Any>> = [:]
    
    // 私有化构造方法
    private override init() {
    }
 
    public func store(message message:Dictionary<String,Any>){
        let fromUserId:String = message["fromUserId"] as! String
        let toUserId:String = message["toUserId"] as! String
        
        var key:String
        if(fromUserId == CacheManager.shared.getUserId()){// 发送人是自己
            key = toUserId
        } else {
            key = fromUserId
        }
        var messageList:Array<Any>
        if(messageDic[key] != nil ){
            messageList = messageDic[key]!
        }else{
            messageList = Array<Any>()
        }
        messageList.append(message)
        messageDic[key] = messageList
    }
    
}
