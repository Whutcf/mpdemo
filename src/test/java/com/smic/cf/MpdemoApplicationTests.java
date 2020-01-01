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
import com.smic.cf.entity.MpUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpdemoApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void select() {
		List<MpUser> mpUsers = userMapper.selectList(null);
		for (MpUser mpUser : mpUsers) {
			System.out.println(mpUser.toString());
		}
	}

	@Test
	public void insert() {
		MpUser mpUser = new MpUser();
		mpUser.setRealName("小米");
		mpUser.setAge(30);
		mpUser.setEmail("xiaomi@smic.com");
		mpUser.setManagerId(1088248166370832385L);
		mpUser.setCreateTime(LocalDateTime.now());
		mpUser.setRemark("我是一个备注，不仅进入数据库");
		int rows = userMapper.insert(mpUser);
		System.out.println(rows);
	}

	@Test
	public void selects() {
		MpUser mpUser = userMapper.selectById(1087982257332887553L);
		System.out.println(mpUser.toString());

		System.out.println("-------------------------------------------");

		List<Long> ids = Arrays.asList(1088248166370832385L, 1211245290259050498L, 1211248550713847809L);
		List<MpUser> mpUsers = userMapper.selectBatchIds(ids);
		mpUsers.forEach(System.out::print);

		System.out.println("-------------------------------------------");

		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("name", "王天风");
		columnMap.put("age", 25);
		List<MpUser> listmp = userMapper.selectByMap(columnMap);
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
		QueryWrapper<MpUser> queryWrapper = new QueryWrapper<>();
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
				.select(MpUser.class,
						info -> !info.getColumn().equals("manager_id") && !info.getColumn().equals("create_time"))
				.like("name", "雨").lt("age", 40);
		List<MpUser> mpUsers = userMapper.selectList(queryWrapper);
		mpUsers.forEach(System.out::println);
	}

	@Test
	public void testCondition() {
		String name = "";
		String email = "x";
		condition(name, email);

	}

	private void condition(String name, String email) {
		QueryWrapper<MpUser> queryWrapper = new QueryWrapper<MpUser>();
//		if(!StringUtils.isEmpty(name)) {
//			queryWrapper.like("name", name);
//		}
//		if(!StringUtils.isEmpty(email)) {
//			queryWrapper.like("email", email);
//		}

		queryWrapper.like(StringUtils.isNotEmpty(name), "name", name).like(StringUtils.isNotEmpty(email), "email",
				email);

		List<MpUser> mpUsers = userMapper.selectList(queryWrapper);
		mpUsers.forEach(System.out::println);
	}

	@Test
	public void selectByWrapperEntity() {
		MpUser whereUser = new MpUser();
		whereUser.setRealName("张三");
		whereUser.setAge(20);
		QueryWrapper<MpUser> queryWrapper = new QueryWrapper<MpUser>(whereUser);
		List<MpUser> mpUsers = userMapper.selectList(queryWrapper);
		mpUsers.forEach(System.out::println);
	}

	@Test
	public void selectByWrapperAllEq() {
		QueryWrapper<MpUser> queryWrapper = new QueryWrapper<MpUser>();
		Map<String, Object> params = new HashMap<>();
		params.put("name", "李四");
		params.put("age", 31);
		params.put("email", null);
//		queryWrapper.allEq(params,false);//自动过滤null的字段
		queryWrapper.allEq((k, v) -> k.equals("name"), params);// 只查询k选中的内容
		List<MpUser> mpUsers = userMapper.selectList(queryWrapper);
		mpUsers.forEach(System.out::println);

	}

	/**
	 * 11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。 并且只取年龄总和小于500的组。 select avg(age)
	 * avg_age,min(age) min_age,max(age) max_age from user group by manager_id
	 * having sum(age) <500
	 * 
	 */
	@Test
	public void selectByWrapperMaps() {
		QueryWrapper<MpUser> queryWrapper = new QueryWrapper<MpUser>();
//		queryWrapper.select("user_id","name").like("name", "王").lt("age", 40);

		queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age").groupBy("manager_id")
				.having("sum(age)<{0}", 500);

		List<Map<String, Object>> selectMaps = userMapper.selectMaps(queryWrapper);
		selectMaps.forEach(System.out::println);
	}

	@Test
	public void selectLambda() {
		LambdaQueryWrapper<MpUser> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.lt(MpUser::getAge, "25").likeRight(MpUser::getRealName, "王");
		List<MpUser> mpUsers = userMapper.selectList(lambdaQuery);
		mpUsers.forEach(System.out::println);

	}

	/**
	 * 5、名字为王姓并且（年龄小于40或邮箱不为空） name like '王%' and(age<40 or email is not null)
	 */
	@Test
	public void selectLambda2() {
		LambdaQueryWrapper<MpUser> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(MpUser::getRealName, "王").and(lq -> lq.lt(MpUser::getAge, 40).or().isNotNull(MpUser::getEmail));
		List<MpUser> mpUsers = userMapper.selectList(lambdaQuery);
		mpUsers.forEach(System.out::println);
	}

	@Test
	public void selectLambda3() {
		List<MpUser> mpUsers = new LambdaQueryChainWrapper<MpUser>(userMapper).like(MpUser::getRealName, "雨")
				.ge(MpUser::getAge, 16).list();
		mpUsers.forEach(System.out::println);
	}

	/**
	 * 自定义方法
	 */
	@Test
	public void selectAllTest() {
		LambdaQueryWrapper<MpUser> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(MpUser::getRealName, "王").and(lq -> lq.lt(MpUser::getAge, 40).or().isNotNull(MpUser::getEmail));
		List<MpUser> mpUsers = userMapper.selectAll(lambdaQuery);
		mpUsers.forEach(System.out::println);
	}

	/**
	 * 5、名字为王姓并且（年龄小于40或邮箱不为空） name like '王%' and(age<40 or email is not null)
	 */
	@Test
	public void selectPage() {
		LambdaQueryWrapper<MpUser> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(MpUser::getRealName, "王").and(lq -> lq.lt(MpUser::getAge, 40).or().isNotNull(MpUser::getEmail));

//		Page<User> page = new Page<>(1, 2);
		Page<MpUser> page = new Page<>(1, 2, false);// false表示不查询记录数

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
		LambdaQueryWrapper<MpUser> lambdaQuery = Wrappers.lambdaQuery();// 一定要加泛型
		lambdaQuery.likeRight(MpUser::getRealName, "王").and(lq -> lq.lt(MpUser::getAge, 40).or().isNotNull(MpUser::getEmail));

		Page<MpUser> page = new Page<>(1, 2);
		IPage<MpUser> iPage = userMapper.selectUserPage(page, lambdaQuery);// 自定义sql练习
		System.out.println("总条数：" + iPage.getTotal());
		System.out.println("总页数：" + iPage.getPages());
		iPage.getRecords().forEach(System.out::println);
	}

	/*******************************************************************************************************************************************************************************/
	@Test
	public void updateById() {
		MpUser mpUser = new MpUser();
		mpUser.setUserId(1211245290259050498L);
		mpUser.setAge(21);
		mpUser.setEmail("lisi@smic.com");
		mpUser.setCreateTime(LocalDateTime.now());
		userMapper.updateById(mpUser);
	}

	@Test
	public void updateByWrapper() {
		UpdateWrapper<MpUser> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("name", "张三").eq("user_id", 1211248037419073538L);
		MpUser mpUser = new MpUser();
		mpUser.setAge(24);
		mpUser.setEmail("zhangzheng@smic.com");
		mpUser.setRealName("张真");
		mpUser.setCreateTime(LocalDateTime.now());
		userMapper.update(mpUser, updateWrapper);
	}

	@Test
	public void updateByWrapper1() {
		MpUser whereUser = new MpUser();
		whereUser.setRealName("张真");
		whereUser.setUserId(1211248037419073538L);
		UpdateWrapper<MpUser> updateWrapper = new UpdateWrapper<>(whereUser);
//		updateWrapper.eq("name", "张三").eq("user_id", 1211248037419073538L);
		MpUser mpUser = new MpUser();
		mpUser.setAge(24);
		mpUser.setEmail("zhangzheng@smic.com");
		mpUser.setRealName("张真");
		mpUser.setCreateTime(LocalDateTime.now());
		userMapper.update(mpUser, updateWrapper);
	}

	@Test
	public void updateByWrapper2() {
		UpdateWrapper<MpUser> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("name", "张真").eq("user_id", 1211248037419073538L).set("age", 20).set("create_time",
				LocalDateTime.now());
		userMapper.update(null, updateWrapper);
	}

	@Test
	public void updateWrapperLambda() {

		LambdaUpdateWrapper<MpUser> lambdaUpdate = Wrappers.lambdaUpdate();
		lambdaUpdate.eq(MpUser::getRealName, "张真").set(MpUser::getAge, 30);
		userMapper.update(null, lambdaUpdate);
	}

	@Test
	public void updateWrapperLambdaChain() {

		boolean update = new LambdaUpdateChainWrapper<MpUser>(userMapper).eq(MpUser::getRealName, "张真")
				.set(MpUser::getAge, 28).update();
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
		LambdaQueryWrapper<MpUser> lambdaQuery = Wrappers.lambdaQuery();
		lambdaQuery.eq(MpUser::getRealName, "王二");
		userMapper.delete(lambdaQuery);
	}

	@Test
	public void testPrimaryKey() {
		MpUser mpUser = new MpUser();
		mpUser.setRealName("西瓜味的棉花");
		mpUser.setAge(24);
		mpUser.setCreateTime(LocalDateTime.now());
		mpUser.setEmail("Yilei_ma@smic.com");
		userMapper.insert(mpUser);
		System.out.println("生成的主键：" + mpUser.getUserId());
	}
}
