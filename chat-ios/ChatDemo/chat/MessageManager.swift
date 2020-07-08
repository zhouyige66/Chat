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

    @objc dynamic var messageDic:Dictionary<String, Array<ChatMessage>> = [:]
    
    // 私有化构造方法
    private override init() {
    }
 
    public func cache(_ message: ChatMessage){
        print("缓存消息")
        let fromUserId:Int64 = message.fromUserId
        let toUserId:Int64 = message.toUserId
        var key:String
        if(message.chatMessageType == ChatMessageType.GROUP){
            key = "g_\(toUserId)"
        }else{
            if(fromUserId == CacheManager.shared.getUserId()){
                key = "f_\(toUserId)"
            }else{
                key = "f_\(fromUserId)"
            }
        }
    
        var messageList:Array<ChatMessage>
        if(messageDic[key] != nil ){
            messageList = messageDic[key]!
        }else{
            messageList = Array()
        }
        messageList.append(message)
        messageDic[key] = messageList
        print("消息容量：\(messageDic)")
    }
    
    public func getMessageList(_ contact:Contact)->Array<ChatMessage>?{
        print("对话id：\(contact.getContactKey())")
        let key = contact.getContactKey()
        return messageDic[key]
    }
    
}
