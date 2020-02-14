//
//  BaseUIViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/15.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit

class BaseUIViewController: UIViewController ,MBProgressHUDDelegate{
    var loadingDialog:MBProgressHUD!
    var toast:MBProgressHUD!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 设置状态栏颜色
        // UIApplication.shared.statusBarStyle = .lightContent
        // 去掉返回按钮上文字信息
        let backItem:UIBarButtonItem = UIBarButtonItem();
        backItem.title = ""
        self.navigationItem.backBarButtonItem = backItem
        
        initLoadingDialog()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    
    func initLoadingDialog(){
        loadingDialog = MBProgressHUD.init(view:self.view)
        self.view.addSubview(loadingDialog!)
        loadingDialog!.mode = MBProgressHUDMode.indeterminate
        loadingDialog!.backgroundView.color = UIColor.black.withAlphaComponent(0.4)
        loadingDialog!.label.text = "正在加载..."
        loadingDialog!.delegate = self
        
        // 初始化toast
        toast = MBProgressHUD.init(view:self.view)
        self.view.addSubview(toast!)
        toast!.mode = MBProgressHUDMode.text
        toast!.offset.y = self.view.frame.height * 0.5 - 100
        toast!.label.text = "提示信息"
    }
    
    func showLoadingDialog(){
        loadingDialog.show(animated: true)
    }
    
    func showLoadingDialog(tipMessage msg:String){
        loadingDialog.label.text = msg
        loadingDialog.show(animated: true)
    }
    
    func hideLoadingDialog(){
        loadingDialog.hide(animated: true)
    }
    
    func showToast(_ msg:String){
        toast?.label.text = msg
        toast?.show(animated: true)
        toast?.hide(animated: true, afterDelay: 2)
    }
}
