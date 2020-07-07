//
//  MainController.swift
//  ChatDemo
//
//  Created by zzy on 2020/7/7.
//  Copyright © 2020 pwc. All rights reserved.
//

import UIKit

class MainViewController:UITabBarController {
    @IBOutlet weak var nav_right: UIBarButtonItem!
    
    @IBAction func connectServer(_ sender: Any) {
        if(chatClient.connectServerSuccess){
            chatClient.disConnectServer()
        }else{
            chatClient.connectServer()
        }
    }
    
    private var chatClient = ChatClient.shared
    private var chatUserManager = ChatUserManager.shared
    private var toUser:User?
    
    deinit {
        chatClient.removeObserver(self, forKeyPath: "connectServerSuccess")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.hidesBackButton = true
        self.navigationItem.title = chatClient.connectServerSuccess ? "已连接" : "未连接"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.navigationItem.title = "未连接"
        self.nav_right.title = "连接"
        
        chatClient.addObserver(self, forKeyPath: "connectServerSuccess", options: .new, context: nil)
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        
        DispatchQueue.main.async {
            if(object is ChatClient){
                self.navigationItem.title = self.chatClient.connectServerSuccess ? "已连接" : "未连接"
                self.nav_right.title = self.chatClient.connectServerSuccess ? "断开" : "连接"
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "jump"){
            let controller:ChatViewController = segue.destination as! ChatViewController
            controller.chatUser = toUser
        }
    }
}
