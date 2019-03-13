//
//  ViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/9.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit

class MainViewController: UIViewController,UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var nav_title: UINavigationItem!
    @IBOutlet weak var nav_right: UIBarButtonItem!
    @IBOutlet weak var tv_user_list: UITableView!
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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
      
        nav_title.title = "未连接"
        nav_right.title = "连接"
        tv_user_list.dataSource = self
        tv_user_list.delegate = self
        tv_user_list.tableFooterView = UIView.init(frame: CGRect.zero)
        
        chatClient.addObserver(self, forKeyPath: "connectServerSuccess", options: .new, context: nil)
        chatUserManager.addObserver(self, forKeyPath: "userList", options: .new, context: nil)
    }

    override func viewWillAppear(_ animated: Bool) {
        nav_title.title = chatClient.connectServerSuccess ? "已连接" : "未连接"
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        
        DispatchQueue.main.async {
            if(object is ChatClient){
                self.nav_title.title = self.chatClient.connectServerSuccess ? "已连接" : "未连接"
                self.nav_right.title = self.chatClient.connectServerSuccess ? "断开" : "连接"
            }else{
                self.tv_user_list.reloadData()
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "jump"){
            let controller:ChatViewController = segue.destination as! ChatViewController
            controller.chatUser = toUser
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return chatUserManager.userList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell",for: indexPath)
        cell.textLabel?.text = chatUserManager.userList[indexPath.row].name
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        toUser = chatUserManager.userList[indexPath.row]
        nav_title.title = "返回"
        self.performSegue(withIdentifier: "jump", sender: self)
    }
    
    deinit {
        chatClient.removeObserver(self, forKeyPath: "connectServerSuccess")
        chatUserManager.removeObserver(self, forKeyPath: "userList")
    }
    
}

