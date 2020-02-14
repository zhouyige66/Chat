//
//  CommonUtil.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/23.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation

final class CommonUtil {
    
    class func int2Bytes(originValue value:UInt32)->[UInt8] {
        var bytes:[UInt8] = [0,0,0,0]
        bytes[0] = UInt8((value >> 24) & 0xFF)
        bytes[1] = UInt8((value >> 16) & 0xFF)
        bytes[2] = UInt8((value >> 8) & 0xFF)
        bytes[3] = UInt8(value & 0xFF)
        return bytes
    }
    
    class func bytes2Int(originBytes bytes:[UInt8] ) ->UInt32 {
        if(bytes.count < 4){
            return 0
        }
        let value1 = UInt32(bytes[0]) << 24
        let value2 = UInt32(bytes[1]) << 16
        let value3 = UInt32(bytes[2]) << 8
        let value4 = UInt32(bytes[3])
        let value = value1 + value2 + value3 + value4
        return value
    }
    
    class func data2Int(originData data:Data) ->UInt32 {
        if(data.count < 4){
            return 0
        }
        let value1 = UInt32(data[0]) << 24
        let value2 = UInt32(data[1]) << 16
        let value3 = UInt32(data[2]) << 8
        let value4 = UInt32(data[3])
        let value = value1 + value2 + value3 + value4
        return value
    }
    
}
