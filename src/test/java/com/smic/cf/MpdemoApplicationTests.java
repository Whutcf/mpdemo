package com.smic.cf;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.smic.cf.dao.UserMapper;
import com.smic.cf.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpdemoApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void select() {
		List<User> users = userMapper.selectList(null);
		for (User user : users) {
			System.out.println(user.toString());
		}
	}

	@Test
	public void insert() {
		User user = new User();
		user.setRealName("小米");
		user.setAge(30);
		user.setEmail("xiaomi@smic.com");
		user.setManagerId(1088248166370832385L);
		user.setCreateTime(LocalDateTime.now());
		user.setRemark("我是一个备注，不仅进入数据库");
		int rows = userMapper.insert(user);
		System.out.println(rows);
	}

	@Test
	public void selects() {
		User user = userMapper.selectById(1087982257332887553L);
		System.out.println(user.toString());

		System.out.println("-------------------------------------------");

		List<Long> ids = Arrays.asList(1088248166370832385L, 1211245290259050498L, 1211248550713847809L);
		List<User> users = userMapper.selectBatchIds(ids);
		users.forEach(System.out::print);

		System.out.println("-------------------------------------------");

		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("name", "王天风");
		columnMap.put("age", 25);
		List<User> listmp = userMapper.selectByMap(columnMap);
		listmp.forEach(System.out::print);
	}

	/**
	 * 1、名字中包含雨并且年龄小于40 name like '%雨%' and age<40
	 * 2、名字中包含雨年并且龄大于等于20且小于等于40并且email不为空 name like '%雨%' and age between 20 and 40
	 * and email is not null 3、名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列 name like '王%'
	 * or age>=25 order by age desc,id asc 4、创建日期为2019年2月14日并且直属上级为名字为王姓
	 * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id
	 * from user where name like '王%') 5、名字为王姓并且（年龄小于40或邮箱不为空） name like '王%'
	 * and(age<40 or email is not null) 6、名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空） name like
	 * '王%' or (age<40 and age>20 and email is not null) 7、（年龄小于40或邮箱不为空）并且名字为王姓
	 * (age<40 or email is not null) and name like '王%' 8、年龄为30、31、34、35 age
	 * in(30、31、34、35) 9、只返回满足条件的其中一条语句即可 limit 1 10、名字中包含雨并且年龄小于40(需求1加强版)
	 * 第一种情况：select id,name from user where name like '%雨%' and age<40 第二种情况：select
	 * id,name,age,email from user where name like '%雨%' and age<40
	 * 
	 */

	@Test
	public void selectByWrapper() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//		QueryWrapper<User> queryWrapper2 = Wrappers.query();
//		queryWrapper.like("name", "雨").lt("age", 40);	
//		queryWrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");
//		queryWrapper.likeRight("name","王").or().ge("age", 25).orderByDesc("age").orderByAsc("user_id") ;
//		queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}", "2019-02-14").inSql("manager_id", "select user_id from mp_user where name like '王%'");
//		queryWrapper.likeRight("name", "王").and(qw->qw.lt("age", 40).or().isNotNull("email"));
//		queryWrapper.likeRight("name", "王").or(qw->qw.lt("age",40).gt("age", 20).isNotNull("email"));
//		queryWrapper.nested(qw->qw.lt("age",40).or().isNotNull("email")).likeRight("name", "王");
//		queryWrapper.in("age", 30,31,34,35);
//		queryWrapper.in("age", Arrays.asList(30,31,34,35));
//		queryWrapper.last("limit 1");
//		queryWrapper.select("user_id","name","age").like("name","雨").lt("age",40);
		queryWrapper
				.select(User.class,
						info -> !info.getColumn().equals("manager_id") && !info.getColumn().equals("create_time"))
				.like("name", "雨").lt("age", 40);
		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void testCondition() {
		String name = "";
		String email = "x";
		condition(name, email);

	}

	private void condition(String name, String email) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//		if(!StringUtils.isEmpty(name)) {
//			queryWrapper.like("name", name);
//		}
//		if(!StringUtils.isEmpty(email)) {
//			queryWrapper.like("email", email);
//		}

		queryWrapper.like(StringUtils.isNotEmpty(name), "name", name).like(StringUtils.isNotEmpty(email), "email",
				email);

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void selectByWrapperEntity() {
		User whereUser = new User();
		whereUser.setRealName("张三");
		whereUser.setAge(20);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(whereUser);
		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void selectByWrapperAllEq() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		Map<String, Object> params = new HashMap<>();
		params.put("name", "李四");
		params.put("age", 31);
		params.put("email", null);
//		queryWrapper.allEq(params,false);//自动过滤null的字段
		queryWrapper.allEq((k, v) -> k.equals("name"), params);// 只查询k选中的内容
		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);

	}

	/**
	 * 11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。 并且只取年龄总和小于500的组。 select avg(age)
	 * avg_age,min(age) min_age,max(age) max_age from user group by manager_id
	 * having sum(age) <500
	 * 
	 */
	@Test
	public void selectByWrapperMaps() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//		queryWrapper.select("user_id","name").like("name", "王").lt("age", 40);

		queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age").groupBy("manager_id")
				.having("sum(age)<{0}", 500);

		List<Map<String, Object>> selectMaps = userMapper.selectMaps(queryWrapper);
		selectMaps.forEach(System.out::println);
	}

	@Test
	public void selectLambda() {
		LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.lt(User::getAge, "25").likeRight(User::getRealName, "王");
		List<User> users = userMapper.selectList(lambdaQuery);
		users.forEach(System.out::println);

	}

	/**
	 * 5、名字为王姓并且（年龄小于40或邮箱不为空） name like '王%' and(age<40 or email is not null)
	 */
	@Test
	public void selectLambda2() {
		LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(User::getRealName, "王").and(lq -> lq.lt(User::getAge, 40).or().isNotNull(User::getEmail));
		List<User> users = userMapper.selectList(lambdaQuery);
		users.forEach(System.out::println);
	}

	@Test
	public void selectLambda3() {
		List<User> users = new LambdaQueryChainWrapper<User>(userMapper).like(User::getRealName, "雨")
				.ge(User::getAge, 16).list();
		users.forEach(System.out::println);
	}

	/**
	 * 自定义方法
	 */
	@Test
	public void selectAllTest() {
		LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(User::getRealName, "王").and(lq -> lq.lt(User::getAge, 40).or().isNotNull(User::getEmail));
		List<User> users = userMapper.selectAll(lambdaQuery);
		users.forEach(System.out::println);
	}

	/**
	 * 5、名字为王姓并且（年龄小于40或邮箱不为空） name like '王%' and(age<40 or email is not null)
	 */
	@Test
	public void selectPage() {
		LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(User::getRealName, "王").and(lq -> lq.lt(User::getAge, 40).or().isNotNull(User::getEmail));

//		Page<User> page = new Page<>(1, 2);
		Page<User> page = new Page<>(1, 2, false);// false表示不查询记录数

//		IPage<User> iPage = userMapper.selectPage(page, lambdaQuery);		
//		System.out.println("总条数："+iPage.getTotal());
//		System.out.println("总页数："+iPage.getPages());

		IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(page, lambdaQuery);
		System.out.println("总条数：" + iPage.getTotal());
		System.out.println("总页数：" + iPage.getPages());
		iPage.getRecords().forEach(System.out::println);
	}

	@Test
	public void selectUserPageTest() {
		LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(User::getRealName, "王").and(lq -> lq.lt(User::getAge, 40).or().isNotNull(User::getEmail));

		Page<User> page = new Page<>(1, 2);
		IPage<User> iPage = userMapper.selectUserPage(page, lambdaQuery);// 自定义sql练习
		System.out.println("总条数：" + iPage.getTotal());
		System.out.println("总页数：" + iPage.getPages());
		iPage.getRecords().forEach(System.out::println);
	}

	/*******************************************************************************************************************************************************************************/
	@Test
	public void updateById() {
		User user = new User();
		user.setUserId(1211245290259050498L);
		user.setAge(21);
		user.setEmail("lisi@smic.com");
		user.setCreateTime(LocalDateTime.now());
		userMapper.updateById(user);
	}

	@Test
	public void updateByWrapper() {
		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("name", "张三").eq("user_id", 1211248037419073538L);
		User user = new User();
		user.setAge(24);
		user.setEmail("zhangzheng@smic.com");
		user.setRealName("张真");
		user.setCreateTime(LocalDateTime.now());
		userMapper.update(user, updateWrapper);
	}

	@Test
	public void updateByWrapper1() {
		User whereUser = new User();
		whereUser.setRealName("张真");
		whereUser.setUserId(1211248037419073538L);
		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>(whereUser);
//		updateWrapper.eq("name", "张三").eq("user_id", 1211248037419073538L);
		User user = new User();
		user.setAge(24);
		user.setEmail("zhangzheng@smic.com");
		user.setRealName("张真");
		user.setCreateTime(LocalDateTime.now());
		userMapper.update(user, updateWrapper);
	}

	@Test
	public void updateByWrapper2() {
		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("name", "张真").eq("user_id", 1211248037419073538L).set("age", 20).set("create_time",
				LocalDateTime.now());
		userMapper.update(null, updateWrapper);
	}

	@Test
	public void updateWrapperLambda() {

		LambdaUpdateWrapper<User> lambdaUpdate = Wrappers.lambdaUpdate();
		lambdaUpdate.eq(User::getRealName, "张真").set(User::getAge, 30);
		userMapper.update(null, lambdaUpdate);
	}

	@Test
	public void updateWrapperLambdaChain() {

		boolean update = new LambdaUpdateChainWrapper<User>(userMapper).eq(User::getRealName, "张真")
				.set(User::getAge, 28).update();
		System.out.println(update);
	}

	/************************
	 * delete
	 *************************************************************/
	@Test
	public void deleteById() {
		userMapper.deleteById(121212121212121212L);
	}

	@Test
	public void deleteWrapper() {
		LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
		lambdaQuery.eq(User::getRealName, "王二");
		userMapper.delete(lambdaQuery);
	}

	@Test
	public void testPrimaryKey() {
		User user = new User();
		user.setRealName("西瓜味的棉花");
		user.setAge(24);
		user.setCreateTime(LocalDateTime.now());
		user.setEmail("Yilei_ma@smic.com");
		userMapper.insert(user);
		System.out.println("生成的主键：" + user.getUserId());
	}
}
