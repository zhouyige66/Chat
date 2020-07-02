//
//  LoginViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit
import Toast_Swift

class LoginViewController: BaseUIViewController {
    @IBOutlet weak var utf_user_name: UITextField!
    @IBOutlet weak var utf_user_password: UITextField!
    @IBAction func register(_ sender: UIButton) {
        self.view.makeToast("该功能还在开发中")
    }
    @IBAction func forgetPassword(_ sender: Any) {
        self.view.makeToast("该功能还在开发中")
    }
    @IBAction func login(_ sender: Any) {
        let userName = utf_user_name.text
        if (userName?.isEmpty)!{
            self.view.makeToast("用户名不能为空！")
            return
        }
        let userPassword = utf_user_password.text
        if (userPassword?.isEmpty)!{
            self.view.makeToast("密码不能为空！")
            return
        }
        
        showLoadingDialog(tipMessage: "正在登录...")
        let loginRequest:Model = Model(success: {data in
            self.hideLoadingDialog()
            UserDefaults.standard.set(self.utf_user_name.text, forKey: "loginName")
            UserDefaults.standard.set(self.utf_user_password.text, forKey: "loginPassword")
            CacheManager.shared.cacheUser(data)
            self.performSegue(withIdentifier: "loginSuccess", sender: self)
        }, fail: {(code,msg) in
            self.hideLoadingDialog()
            self.showToast("错误码:\(code)，错误信息：\(msg)")
        })
        var requestData:Dictionary<String,Any> = [:]
        requestData["userName"] = userName
        requestData["password"] = userPassword
        loginRequest.doPost(ApiConfig.login, requestData, "登录验证")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        utf_user_name.text = UserDefaults.standard.string(forKey: "loginName")
        utf_user_password.text = UserDefaults.standard.string(forKey: "loginPassword")
    }

}
