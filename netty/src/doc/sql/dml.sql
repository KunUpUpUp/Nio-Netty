INSERT INTO chatroom.t_group (group_name,owner,user_ids,create_time,update_time,is_deleted) VALUES
	 ('两人群','1','1,2','2025-04-01 17:14:06','2025-04-01 20:30:23',0),
	 ('三人游','1','1,2,3','2025-04-01 20:40:42','2025-04-01 20:40:42',0);

INSERT INTO chatroom.t_user (username,password,nickname,birthday,register_time,last_login_time,create_time,update_time,is_online,is_deleted) VALUES
     ('zkp','123','请给我一瓶养乐多','1999-12-06','2025-03-31 14:37:31','2025-04-02 19:38:43','2025-03-31 14:37:31','2025-04-02 19:38:43',1,0),
     ('wl','123','木木','1999-09-11','2025-04-01 10:42:37','2025-04-02 19:39:56','2025-04-01 10:42:37','2025-04-02 19:39:56',1,0);
