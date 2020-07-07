//
//  ViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/9.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit

class RecentChatViewController: BaseUIViewController,UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var tv_user_list:UITableView!

    private var chatClient = ChatClient.shared
    private var chatUserManager = ChatUserManager.shared
    private var toUser:User?
    
    deinit {
        chatUserManager.removeObserver(self, forKeyPath: "userList")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        tv_user_list.dataSource = self
        tv_user_list.delegate = self
        tv_user_list.tableFooterView = UIView.init(frame: CGRect.zero)
        
        // kvo
        chatUserManager.addObserver(self, forKeyPath: "userList", options: .new, context: nil)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.hidesBackButton = true
        self.navigationItem.title = chatClient.connectServerSuccess ? "已连接" : "未连接"
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        
        DispatchQueue.main.async {
            self.tv_user_list.reloadData()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "jump"){
            // 跳转传值
            let controller:ChatViewController = segue.destination as! ChatViewController
            controller.chatUser = toUser
        }
    }
 
    /**tableView相关**/
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
        self.navigationItem.title = "返回"
        self.performSegue(withIdentifier: "jump", sender: self)
    }
        
}

