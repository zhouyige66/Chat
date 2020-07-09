//
//  ViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/9.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit

class ContactListViewController: BaseUIViewController,UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var tableView: UITableView!
    
    private var chatClient = ChatClient.shared
    private var chatUserManager = ChatUserManager.shared
    private var currentContact:Contact?
    
    deinit {
        chatUserManager.removeObserver(self, forKeyPath: ChatUserManager.observerable)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        
        // kvo
        chatUserManager.addObserver(self, forKeyPath: ChatUserManager.observerable, options: .new, context: nil)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.hidesBackButton = true
        self.navigationItem.title = chatClient.connectServerSuccess ? "已连接" : "未连接"
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        DispatchQueue.main.async {
            print("收到更新最近联系人通知")
            self.tableView.reloadData()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "jump"){
            // 跳转传值
            let controller:ChatViewController = segue.destination as! ChatViewController
            controller.contact = currentContact
        }
    }
    
    /**tableView相关**/
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return chatUserManager.contactList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let contact = chatUserManager.contactList[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell",for: indexPath)
        cell.textLabel?.text = contact.getContactName()
        cell.detailTextLabel?.text = contact.getLatestMsg()
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        currentContact = chatUserManager.contactList[indexPath.row]
        self.navigationItem.title = "返回"
        self.performSegue(withIdentifier: "jump", sender: self)
    }
    
}

