//
//  JsonUtil.swift
//  ChatDemo
//
//  Created by zzy on 2020/7/7.
//  Copyright © 2020 pwc. All rights reserved.
//

import Foundation

class JsonUtil{

    static func json2Model<D:Decodable>(_ jsonStr:String,_ type:D.Type)->D{
        let data:Data = jsonStr.data(using: String.Encoding.utf8)!
        return jsonData2Model(data, type)
    }
    
    static func jsonData2Model<D:Decodable>(_ jsonData:Data,_ type:D.Type)->D{
        let decoder:JSONDecoder = JSONDecoder()
        let model = try! decoder.decode(type, from: jsonData)
        return model
    }
    
}
