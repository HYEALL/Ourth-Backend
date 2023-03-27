use ourth;

insert into mission(text, point)
values ('Turn off the lights for 1 hour', 200),
       ('Eat a vegetable meal', 200),
       ('Shower in 20 minutes', 100),
       ('Blogging about the environment', 200),
       ('Eat without leaving any prepared food', 200),
       ('Buy used books', 100),
       ('Walk if your destination is nearby', 200),
       ('Pick up 3 or more trash while jogging', 300),
       ('Using eco bags when shopping', 200),
       ('Donate used items', 200),
       ('Using a cup when brushing your teeth', 200),
       ('Using a laundry net when washing', 100),
       ('Cover the pot when cooking', 100),
       ('No straws in cafes', 200),
       ('Going out with a tumbler today', 200),
       ('Learn to cook and try', 200),
       ('Maintaining the right room temperature', 200);

insert into school(school_name)
values ('Ajou University'),
       ('Chungang University'),
       ('Daejin University'),
       ('Dong-a University'),
       ('Dong-Eui University'),
       ('Ewha Womans University'),
       ('Gwangju Institute of Science and Technology'),
       ('Hankuk University of Foreign Studies'),
       ('Hanyang University'),
       ('Hongik University'),
       ('Inha University'),
       ('Kookmin University'),
       ('Korea Advanced Institute of Science & Technology'),
       ('Korea University Seoul Campus'),
       ('Kyungpook National University'),
       ('Myongji University'),
       ('Pukyong National University Daeyeon Campus'),
       ('Sahmyook University'),
       ('Sangmyung University'),
       ('Seoul National University of Science and Technology'),
       ('Seoul Women''s University'),
       ('Sookmyung Women''s University'),
       ('Soonchunhyang University'),
       ('Soongsil University'),
       ('Sungkonghoe University'),
       ('Sungkyunkwan University'),
       ('Sungshin Women''s University'),
       ('Tech University of Korea'),
       ('University of Seoul'),
       ('Yonsei University Mirae Campus'),
       ('Yonsei University Seoul Campus');

-- insert into user(email, password, username, school_id, point)
-- values ('kim@naver.com', 1234, '김신아', 25, 0);

-- insert into user_roles(user_user_id, roles)
-- values ('1', 'ADMIN');