CREATE TABLE IF NOT EXISTS `player_info`
(
    `player_id`      int          NOT NULL AUTO_INCREMENT,
    `usr_name`       varchar(255) NOT NULL,
    `usr_pwd`        varchar(255) NOT NULL,
    `player_name`    varchar(255) NULL,
    `update_time`    datetime     NULL,
    `pos_x`          float        NULL,
    `pos_y`          float        NULL,
    `pos_z`          float        NULL,
    `roa_x`          float        NULL,
    `roa_y`          float        NULL,
    `roa_z`          float        NULL,
    `move_speed`     float        NULL,
    `rotate_speed`   float        NULL,
    `is_running`     int          NULL,
    `is_talking`     int          NULL,
    `talk_msg`       text         NULL,
    `scene_id`       int          NULL,
    `activity_id`    int          NULL,
    `identity_name`  int          NULL,
    `hair_type`      int          NULL,
    `face_type`      int          NULL,
    `hair_color`     int          NULL,
    `face_color`     int          NULL,
    `hobby`          varchar(255) NULL,
    `disposition`    varchar(255) NULL,
    `occupation`     varchar(255) NULL,
    `story`          varchar(255) NULL,
    `blouse_index`   int          NULL,
    `trousers_index` int          NULL,
    `shoe_index`     int          NULL,
    `role`           varchar(255) NULL,
    PRIMARY KEY (`player_id`),
    UNIQUE INDEX `register` (`usr_name`),
    INDEX `query` (`player_id`, `usr_name`, `scene_id`, `activity_id`)
);

CREATE TABLE IF NOT EXISTS `user_friend_info`
(
    `uf_id`     int NOT NULL AUTO_INCREMENT,
    `user_id`   int NOT NULL,
    `friend_id` int NOT NULL,
    PRIMARY KEY (`uf_id`),
    UNIQUE INDEX `friend` (`user_id`, `friend_id`)
);

CREATE TABLE IF NOT EXISTS `apply_record`
(
    `apply_id`   int      NOT NULL AUTO_INCREMENT,
    `applyer`    int      NOT NULL,
    `receiver`   int      NOT NULL,
    `apply_time` datetime NOT NULL,
    `result`     int      NOT NULL,
    PRIMARY KEY (`apply_id`)
);

CREATE TABLE IF NOT EXISTS `activity_info`
(
    `activity_id`         int          NOT NULL AUTO_INCREMENT,
    `player_id`           int          NOT NULL,
    `scene_id`            int          NOT NULL,
    `player_name`         varchar(255) NOT NULL,
    `activity_name`       varchar(255) NOT NULL,
    `create_time`         datetime     NOT NULL,
    `start_time`          datetime     NOT NULL,
    `end_time`            datetime     NOT NULL,
    `activity_type`       int          NOT NULL,
    `activity_permission` int          NOT NULL,
    `activity_password`   varchar(255) NOT NULL,
    `current_player`      int          NULL,
    `max_player`          int          NULL,
    PRIMARY KEY (`activity_id`)
);

CREATE TABLE IF NOT EXISTS `msg_record`
(
    `msg_id`      int      NOT NULL AUTO_INCREMENT,
    `sender_id`   int      NOT NULL,
    `receive_id`  int      NOT NULL,
    `scene_id`    int      NULL,
    `activity_id` int      NULL,
    `create_time` datetime NOT NULL,
    `msg`         text     NOT NULL,
    `is_read`     int      NOT NULL,
    PRIMARY KEY (`msg_id`),
    INDEX `index_all` (`sender_id`, `receive_id`, `scene_id`, `activity_id`),
    CONSTRAINT `msg_record_sender` FOREIGN KEY (`sender_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `msg_record_receiver` FOREIGN KEY (`receive_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `msg_record_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `activity_player`
(
    `ap_id`       int NOT NULL AUTO_INCREMENT,
    `activity_id` int NOT NULL,
    `player_id`   int NOT NULL,
    PRIMARY KEY (`ap_id`),
    UNIQUE INDEX `activity_player_all` (`ap_id`, `activity_id`, `player_id`),
    CONSTRAINT `activity_player_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `activity_player_player` FOREIGN KEY (`player_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `broadcast`
(
    `bc_id`     int          NOT NULL AUTO_INCREMENT,
    `player_id` int          NOT NULL,
    `title`     varchar(255) NOT NULL,
    `content`   text         NOT NULL,
    `send_time` datetime     NOT NULL,
    PRIMARY KEY (`bc_id`)
);

CREATE TABLE IF NOT EXISTS `goods_types`
(
    `type_id` int          NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) NOT NULL,
    PRIMARY KEY (`type_id`),
    UNIQUE INDEX `goods_types_index_all` (`type_id`, `name`)
);

CREATE TABLE IF NOT EXISTS `goods`
(
    `goods_id` int          NOT NULL AUTO_INCREMENT,
    `type_id`  int          NOT NULL,
    `name`     varchar(255) NOT NULL,
    `price`    double       NOT NULL,
    `amount`   int          NOT NULL,
    `stock`    int          NOT NULL,
    `time`     datetime     NOT NULL,
    PRIMARY KEY (`goods_id`),
    CONSTRAINT `items_type_id` FOREIGN KEY (`type_id`) REFERENCES `goods_types` (`type_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    INDEX `items_index_all` (`type_id`, `name`)
);

CREATE TABLE IF NOT EXISTS `goods_record`
(
    `record_id` int      NOT NULL AUTO_INCREMENT,
    `player_id` int      NOT NULL,
    `goods_id`  int      NOT NULL,
    `amount`    int      NOT NULL,
    `time`      datetime NOT NULL,
    PRIMARY KEY (`record_id`),
    CONSTRAINT `items_player_id` FOREIGN KEY (`player_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `items_goods_id` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `equipment_info`
(
    `e_id`        int          NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `perfab_name` varchar(255) NOT NULL,
    `create_time` datetime     NOT NULL,
    PRIMARY KEY (`e_id`)
);

CREATE TABLE IF NOT EXISTS `activity_equipment`
(
    `re_id`          bigint NOT NULL,
    `activity_id`    int    NOT NULL,
    `desk_id`        int    NOT NULL,
    `e_id`           int    NOT NULL,
    `sub_id`         int    NOT NULL,
    `creator_id`     int    NOT NULL,
    `pos_x`          float  NOT NULL,
    `pos_y`          float  NOT NULL,
    `pos_z`          float  NOT NULL,
    `roa_x`          float  NOT NULL,
    `roa_y`          float  NOT NULL,
    `roa_z`          float  NOT NULL,
    `scale_x`        float  NOT NULL,
    `scale_y`        float  NOT NULL,
    `scale_z`        float  NOT NULL,
    `dealing_player` int    NULL,
    PRIMARY KEY (`re_id`),
    UNIQUE INDEX `room_equipment_all` (`re_id`, `activity_id`, `desk_id`, `e_id`, `sub_id`, `creator_id`),
    CONSTRAINT `re_activity_id` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`) ON DELETE CASCADE,
    CONSTRAINT `re_e_id` FOREIGN KEY (`e_id`) REFERENCES `equipment_info` (`e_id`) ON DELETE CASCADE,
    CONSTRAINT `re_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE
);

INSERT IGNORE INTO `player_info`(`usr_name`, `usr_pwd`, `role`)
values ('player', '$2a$10$qh7OQ8eAtaSZGWZW.oExcOPlRPba/DorBF7gtOIiCeDspAFCYkrfi', 'PLAYER');
INSERT IGNORE INTO `player_info`(`usr_name`, `usr_pwd`, `role`)
values ('admin', '$2a$10$owUIS9Zv.R33iw.IiDyjDOrJ.1no8yoJlEwPmGq1hYn78pJftljJW', 'ADMIN');