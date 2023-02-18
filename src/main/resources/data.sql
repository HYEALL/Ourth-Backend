use ourth;

insert into user(email, password, username, region, point)
values ('kim@naver.com', 1234, '김신아', 'Seoul', 35000);

insert into mission(text, point)
values ('1시간 소등하기', 2000),
       ('채소로만 이루어진 식사하기', 2000),
       ('20분 안에 샤워하기', 1000),
       ('환경 보호 관련 블로그 포스팅하기', 2000),
       ('준비한 음식 남기지 않고 먹기', 1000),
       ('중고 서적 구매하기', 1000),
       ('버스 > 택시 이용횟수 준수하기', 2000),
       ('산책하며 쓰레기 3개 이상 줍기', 3000);

insert into user_roles(user_user_id, roles)
values ('1', 'ADMIN');