//
//  HttpUtil.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation
import Alamofire

protocol HttpRequestCallback {
    func onSuccess(data:JSON)
    func onFail(code:Int,chatMessage:String)
}

class BaseModel{
    // 基本配置
    static let sharedSessionManager: Alamofire.SessionManager = {
        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForRequest = 5
        return Alamofire.SessionManager(configuration: configuration)
    }()
    
    var delegate:HttpRequestCallback?
    
    private func addBaseParams(_ requestData:Dictionary<String,Any>)->Dictionary<String,Any> {
        // 添加默认参数
        let userDefaults:UserDefaults = UserDefaults.standard
        let baseData = JSON(parseJSON:userDefaults.string(forKey: "data")!)
        var params = requestData
        params.updateValue(baseData["AccountId"].int!, forKey: "AccountId")
        params.updateValue(baseData["AccountName"].string!, forKey: "AccountName")
        params.updateValue(baseData["UserType"].int!, forKey: "UserType")
        params.updateValue(baseData["OrgNo"].string!, forKey: "OrgNo")
        params.updateValue(baseData["OrgName"].string!, forKey: "OrgName")
        params.updateValue(baseData["Token"].string!, forKey: "Token")
        params.updateValue("zh-cn", forKey: "Language")
        return params
    }
    
    private func request(httpMethod method:HTTPMethod,url urlString:String,requestParam requestData:Dictionary<String,Any>,
                         logTip logText:String){
        // 添加默认参数（公有body部分等）
        //        let params:Dictionary<String,Any> = addBaseParams(requestData)
        // 输出提交参数
        print("\(logText)-->API:\(urlString)")
        print("\(logText)-->提交参数：\(requestData)")
        // 设置超时时间
        let manager = Alamofire.SessionManager.default
        manager.session.configuration.timeoutIntervalForRequest = 5
        let encode:ParameterEncoding = method == .post ? JSONEncoding.default: URLEncoding.default
        Alamofire.request(urlString,method: method,parameters:requestData,encoding:encode).response { (result) in
            guard result.response != nil else {
                let error = result.error?.localizedDescription ?? ""
                print("错误原因：\(error)")
                self.delegate?.onFail(code: -1, chatMessage: "出错：\(error)")
                return
            }
            
            if let data = result.data, let utf8Text = String(data: data, encoding: .utf8) {
                print("\(logText)-->返回结果： \(utf8Text)")
                if utf8Text.count==0{
                    self.delegate?.onFail(code: -1, chatMessage: "返回数据为空")
                    return
                }
                
                let json = JSON(parseJSON:utf8Text)
                if(json["code"].int == 200){
                    self.delegate?.onSuccess(data: json["data"])
                }else{
                    var error:String = "解析错误"
                    if let errorMsg = json["msg"].string{
                        error = errorMsg
                    }
                    self.delegate?.onFail(code: json["code"].int!, chatMessage: error)
                }
            }else{
                print("\(logText)-->出错： \(String(describing: result.error?.localizedDescription))")
                self.delegate?.onFail(code: -1, chatMessage: "\(logText)出错")
            }
        }
    }
    
    func doPost(_ url:String,_ requestData:Dictionary<String,Any>,_ logText:String){
        request(httpMethod: .post, url: url, requestParam: requestData, logTip: logText)
    }
    
    func doGet(_ url:String,_ requestData:Dictionary<String,Any>,_ logText:String){
        request(httpMethod: .get, url: url, requestParam: requestData, logTip: logText)
    }
    
}

class Model:BaseModel,HttpRequestCallback{
    var success:((_ data:JSON)->Void)?
    var fail:((_ code:Int,_ chatMessage:String)->Void)?
    
    init(success successCallback:@escaping (_ data:JSON)->Void,fail failCallback:@escaping (_ code:Int,_ chatMessage:String)->Void) {
        super.init()
        
        self.success = successCallback
        self.fail = failCallback
        
        self.delegate = self
    }
    
    func onSuccess(data: JSON) {
        self.success!(data)
    }
    
    func onFail(code: Int, chatMessage: String) {
        self.fail!(code,chatMessage)
    }
}


