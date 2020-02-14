//
//  CacheUtil.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

final class CacheManager: NSObject {
    static let shared = CacheManager()
    
    var user:JSON?
    
    public func cacheUser(_ user:JSON){
        self.user = user
        UserDefaults.standard.set(user.string, forKey: "userInfo")
        UserDefaults.standard.synchronize()
    }
   
    public func getUser()->JSON{
        return UserDefaults.standard.object(forKey: "userInfo") as! JSON
    }
    
    public func getUserId()->String{
        if(user != nil){
            return user!["id"].string!
        }else{
            return ""
        }
    }
    
    public func getUserName()->String{
        if(user != nil){
            return user!["name"].string!
        }else{
            return ""
        }
    }
}
