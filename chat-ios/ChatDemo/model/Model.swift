//
//  Model.swift
//  ChatDemo
//
//  Created by zzy on 2020/7/6.
//  Copyright © 2020 pwc. All rights reserved.
//

import Foundation

struct Friend:Codable,CGYJSON {
    var id:Int64!
    var name:String!
    var phone:String?
    var email:String?
    var password:String?
    var head:String?
    var isDelete:Bool!
    var createDate:String?
    var modifyDate:String?
    var friendList:String?
    var groupList:String?
}

struct Group: Codable,CGYJSON{
    var id: Int64!
    var name: String!
    var description: String?
    var creatorId: Int64!
    var managerList: String?
    var isDelete: Bool!
    var createDate: String!
    var modifyDate: String!
    var memberList:String!
}
