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