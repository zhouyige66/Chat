//
//  FriendListViewController.swift
//  ChatDemo
//
//  Created by zzy on 2020/7/7.
//  Copyright © 2020 pwc. All rights reserved.
//

import UIKit

class FriendListViewController: BaseUIViewController,UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var tableView: UITableView!

    private var chatClient = ChatClient.shared
    private var chatUserManager = ChatUserManager.shared
    private var currentContact:Contact!
    private var groupList:Array<Group> = []
    private var friendList:Array<Friend> = []
    
    deinit {
        chatUserManager.removeObserver(self, forKeyPath: ChatUserManager.updateOnlineNotify)
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        
        DispatchQueue.main.async {
            print("收到更新好友列表通知")
            print(self.friendList)
            self.tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        
        // 初始化
        groupList = Array()
        friendList = Array()
        
        // kvo
        chatUserManager.addObserver(self, forKeyPath: ChatUserManager.updateOnlineNotify, options: .new, context: nil)
        // 获取好友列表
        getFriendList()
        getGroupList()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.hidesBackButton = true
        self.navigationItem.title = chatClient.connectServerSuccess ? "已连接" : "未连接"
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "jump"){
            // 跳转传值
            let controller:ChatViewController = segue.destination as! ChatViewController
            controller.contact = currentContact
        }
    }
    
    private func getFriendList(){
        showLoadingDialog(tipMessage: "正在加载数据...")
        let request:HttpBlockModel = HttpBlockModel(success: {data in
            self.hideLoadingDialog()
            let rawData = try! data.rawData()
            let friends:Array<Friend> = JsonUtil.jsonData2Model(rawData, Array<Friend>.self) ?? []
            if(friends.count == 0){
                self.showToast("暂未添加好友")
                return
            }
            self.friendList.append(contentsOf: friends)
            self.chatUserManager.friendList = self.friendList
            self.tableView.reloadData()
        }, fail: {(code,msg) in
            self.hideLoadingDialog()
            self.showToast("加载数据出错：\(msg)")
        })
        var requestData:Dictionary<String,Any> = [:]
        requestData["userId"] = CacheManager.shared.getUserId()
        request.doGet(ApiConfig.getFriendList, requestData, "获取好友列表")
    }
    
    private func getGroupList(){
        showLoadingDialog(tipMessage: "正在加载数据...")
        let request:HttpBlockModel = HttpBlockModel(success: {data in
            self.hideLoadingDialog()
            let rawData = try! data.rawData()
            let groups:Array<Group> = JsonUtil.jsonData2Model(rawData, Array<Group>.self) ?? []
            if(groups.count <= 0){
                self.showToast("暂未加入群组")
                return
            }
            self.groupList.append(contentsOf: groups)
            self.chatUserManager.groupList = self.groupList
            self.tableView.reloadData()
        }, fail: {(code,msg) in
            self.hideLoadingDialog()
            self.showToast("加载数据出错：\(msg)")
        })
        var requestData:Dictionary<String,Any> = [:]
        requestData["userId"] = CacheManager.shared.getUserId()
        request.doGet(ApiConfig.getGroupList, requestData, "获取群组列表")
    }
    
    /**tableView相关**/
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String?{
        return section == 0 ? "群组列表" : "好友列表"
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat{
        return 50
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let count = section == 0 ? groupList.count: friendList.count
        return count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell",for: indexPath)
        if(indexPath.section == 0){
            let groupImage = UIImage(named: "ic_chat")
            cell.imageView?.image = groupImage
            let group = groupList[indexPath.row]
            cell.textLabel?.text = group.name
            cell.detailTextLabel?.text = "\(group.memberList.count)人"
        }else{
            let friend = friendList[indexPath.row]
            cell.imageView?.image = UIImage(named: "ic_chat")
            cell.textLabel?.text = friend.name
            cell.detailTextLabel?.text = friend.getOnlineState()
        }

        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // 取消选中
        tableView.deselectRow(at: indexPath, animated: true)
        // 打开聊天页面
        if indexPath.section == 0 {
            currentContact = chatUserManager.addContact(contactType: 1, contactId: groupList[indexPath.row].id)
        } else {
            currentContact = chatUserManager.addContact(contactType: 0, contactId: friendList[indexPath.row].id)
        }
        self.navigationItem.title = "返回"
        self.performSegue(withIdentifier: "jump", sender: self)
    }
        
}
