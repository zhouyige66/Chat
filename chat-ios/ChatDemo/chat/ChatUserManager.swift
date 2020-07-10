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
    static let updateOnlineNotify:String = "onlineUserIdSet"
    static let updateContactNotify:String = "contactList"
    
    var friendList:Array<Friend> {
        didSet{
            print("绑定好友列表")
            for friend in friendList{
                if onlineUserIdSet.contains(friend.id){
                    friend.online = true
                }else{
                    friend.online = false
                }
            }
        }
    }
    var groupList:Array<Group>{
        didSet{
            print("绑定群组列表")
        }
    }
    @objc dynamic var onlineUserIdSet:Set<Int64>{
        didSet{
            print("有用户登录登出了")
            print("在线名单：\(onlineUserIdSet)")
            // 更新在线列表
            guard friendList.count > 0  else {
                print("好友列表为空")
                return
            }
            
            print("好友名单：\(friendList)")
            for friend in friendList{
                let id = friend.id
                if onlineUserIdSet.contains(id!){
                    print("用户\(String(describing: id))在线")
                    friend.online = true
                }else{
                    print("用户\(String(describing: id))不在线")
                    friend.online = false
                }
            }
            print("好友名单更新后：\(friendList)")
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
    
    public func addContact(contactType type:Int,contactId id:Int64) -> Contact?{
        let key = getContactKey(type, id)
        if(contactDic.keys.contains(key)){
            return contactDic[key]!
        }
        
        let contact = Contact()
        contact.type = type
        var find = false
        if(type == 0){
            for friend in friendList{
                if(friend.id == id){
                    contact.friend = friend
                    find = true
                    break
                }
            }
        }else{
            for group in groupList{
                if(group.id == id){
                    contact.group = group
                    find = true
                    break
                }
            }
        }
        
        // 有可能未找到，未找到值放过
        if find {
            contactDic[key] = contact
            contactList.append(contact)
             return contact
        }
        
        return nil
    }
    
    public func addContact(chatMessage message:ChatMessage,receive isRecieve:Bool){
        let fromUserId = message.fromUserId!
        let toUserId = message.toUserId!
        var contact:Contact?
        if(message.chatMessageType == ChatMessageType.GROUP){
            contact = ChatUserManager.shared.addContact(contactType: 1, contactId: toUserId)
        }else{
            if(isRecieve){
                contact = ChatUserManager.shared.addContact(contactType: 0, contactId: fromUserId)
            }else{
                contact = ChatUserManager.shared.addContact(contactType: 0, contactId: toUserId)
            }
        }
        if(contact != nil){
            contact!.latestChatMessage = message
        }
    }
    
    public func getContact(contactType type:Int,contactId id:Int64) ->Contact?{
        let key = getContactKey(type, id)
        if(contactDic.keys.contains(key)){
            return contactDic[key]!
        }
        
        return nil
    }
    
    /// 清空缓存数据
    public func clear(){
        friendList.removeAll()
        groupList.removeAll()
        onlineUserIdSet.removeAll()
        contactList.removeAll()
    }
    
}
