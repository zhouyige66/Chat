//
//  CacheUtil.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation
import SwiftyJSON

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
    
    public func getUserId()->Int64{
        if(user != nil){
            return user!["id"].int64!
        }else{
            return -1
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
