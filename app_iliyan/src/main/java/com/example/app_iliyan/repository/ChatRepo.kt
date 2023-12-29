package com.example.app_iliyan.repository

class ChatRepo {
  val userRepo: UserRepo = UserRepo()
  val groupChatRepo: GroupChatRepo = GroupChatRepo()
  val friendRequestRepo: FriendRequestRepo = FriendRequestRepo()
  val messageRepo: MessageRepo = MessageRepo()
}
