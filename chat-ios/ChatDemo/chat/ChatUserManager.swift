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
    
    var userIdList:Set<String>
    @objc dynamic var userList:Array<User>!
    
    // 私有化构造方法
    private override init() {
        userIdList = []
        userList = []
    }
    
    public func addUser(_ user:User){
        if(userIdList.contains(user.id)){
            return
        }
        
        userIdList.insert(user.id)
        userList.append(user)
    }
    
    public func removeUser(_ user:User){
        if(userIdList.contains(user.id)){
            userIdList.remove(user.id)
            for u in userList{
                if(u.id == user.id){
                    let index:Int = userList.firstIndex(of: u)!
                    userList.remove(at: index)
                }
            }
        }
    }
    
    public func clear(){
        userList.removeAll()
    }
    
}
