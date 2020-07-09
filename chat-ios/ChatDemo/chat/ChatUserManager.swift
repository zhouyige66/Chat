//
//  ChatUserManager.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/11.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

final class ChatUserManager:NSObject {
    static let shared = ChatUserManager()
    static let observerable:String = "contactList"
    
    var friendList:Array<Friend>
    var groupList:Array<Group>
    var onlineUserIdSet:Set<Int64>{
        didSet{
            print("有用户登录登出了")
            // 更新在线列表
            guard friendList.count > 0  else {
                print("好友列表为空")
                return
            }
            
            for var friend in friendList{
                if onlineUserIdSet.contains(friend.id){
                    friend.online = true
                }else{
                    friend.online = false
                }
            }
        }
    }
    var contactDic:Dictionary<String,Contact> = [:]
    @objc dynamic var contactList:Array<Contact>!
    
    /// 私有化构造方法
    private override init() {
        friendList = []
        groupList = []
        onlineUserIdSet = []
        contactList = []
    }
    
    private func getContactKey(_ type:Int,_ id:Int64)->String{
        return type == 0 ? "f_\(id)" : "g_\(id)"
    }
    
    public func addContact(contactType type:Int,contactId id:Int64) -> Contact{
        let key = getContactKey(type, id)
        if(contactDic.keys.contains(key)){
            return contactDic[key]!
        }
        
        let contact = Contact()
        contact.type = type
        if(type == 0){
            for friend in friendList{
                if(friend.id == id){
                    contact.friend = friend
                    break
                }
            }
        }else{
            for group in groupList{
                if(group.id == id){
                    contact.group = group
                    break
                }
            }
        }
        
        contactDic[key] = contact
        contactList.append(contact)
        return contact
    }
    
    public func addContact(chatMessage message:ChatMessage,receive isRecieve:Bool){
        let fromUserId = message.fromUserId!
        let toUserId = message.toUserId!
        var contact:Contact
        if(message.chatMessageType == ChatMessageType.GROUP){
            contact = ChatUserManager.shared.addContact(contactType: 1, contactId: toUserId)
        }else{
            if(isRecieve){
                contact = ChatUserManager.shared.addContact(contactType: 0, contactId: fromUserId)
            }else{
                contact = ChatUserManager.shared.addContact(contactType: 0, contactId: toUserId)
            }
        }
        contact.latestChatMessage = message
    }
    
    /// 清空缓存数据
    public func clear(){
        friendList.removeAll()
        groupList.removeAll()
        onlineUserIdSet.removeAll()
        contactList.removeAll()
    }
    
}
