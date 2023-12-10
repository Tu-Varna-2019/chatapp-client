package com.example.app_iliyan.repository

class ChatRepo {
  public val userRepo: UserRepo = UserRepo()
  public val groupChatRepo: GroupChatRepo = GroupChatRepo()
  public val friendRequestRepo: FriendRequestRepo = FriendRequestRepo()
}
