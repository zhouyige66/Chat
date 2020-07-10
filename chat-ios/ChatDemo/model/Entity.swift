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

class UserInfo:Codable,CGYJSON {
    var id:Int64!
    var name:String!
    var phone:String?
    var email:String?
    var head:String?
    var isDelete:Bool!
    var createDate:String?
    var modifyDate:String?
    var friendList:String?
    var groupList:String?
    var online:Bool?
    
    func getOnlineState() -> String {
        if online != nil && online! {
            return "在线"
        }else {
            return "离线"
        }
    }
    
    func getHead()->String{
        if head == nil {
            return ""
        }
        return ApiConfig.HOST + "/" + head!
    }
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
        
        return "g_\(String(describing: group!.id! ))"
    }
    
    public func getContactHead()->String{
        if(type == 0){
            return ""
        }
        
        guard let head = friend?.head else {
            return ""
        }
        
        print("用户头像\(head)")
        return ApiConfig.HOST + "/" + head
    }
    
    public func getLatestMsg()->String{
        guard latestChatMessage != nil else {
            return ""
        }
        
        var msg = ""
        let bodyType = latestChatMessage?.bodyType
        switch bodyType {
        case .TEXT:
            msg = (latestChatMessage?.getMessageValue())!
            break
        case .IMG:
            msg = "[图片]"
            break
        case .VIDEO:
            msg = "[视频]"
            break
        case .AUDIO:
            msg = "[音频]"
            break
        case .FILE:
            msg = "[文件]"
            break
        case .LINK:
            msg = "[超链接]"
            break
        case .HYBRID:
            msg = "[富文本]"
            break
        case .none:
            msg = ""
        }
        return msg
    }
}
