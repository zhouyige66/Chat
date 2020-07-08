//
//  User.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

class User:NSObject {
    var id:Int64!
    var name:String!
}

class Contact:NSObject {
    var type:Int!
    var friend:Friend?
    var group:Group?
    var latestChatMessage:ChatMessage?
    var latestChatTime:Date?
    
    public func getContactId()->Int64?{
        if(type == 0){
            return friend?.id
        }
        
        return group?.id
    }
    
    public func getContactName()->String?{
        if(type == 0){
            return friend?.name
        }
        
        return group?.name
    }
    
    public func getContactKey()->String{
        if(type == 0){
            return "f_\(String(describing: friend!.id!))"
        }
        
        return "g_\(String(describing: group!.id!))"
    }

}
