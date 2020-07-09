//
//  ApiConfig.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

struct ApiConfig {
    public static let HOST = "http://127.0.0.1:9001";
    
    public static let login = HOST + "/login";// 登录验证
    public static let getFriendList = HOST + "/friend/list";// 好友列表
    public static let getGroupList = HOST + "/group/list";// 群组列表
}
