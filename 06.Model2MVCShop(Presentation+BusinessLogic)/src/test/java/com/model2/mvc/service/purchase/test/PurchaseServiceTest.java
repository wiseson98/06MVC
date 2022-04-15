package com.model2.mvc.service.purchase.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration	(locations = {	"classpath:config/context-common.xml",
										"classpath:config/context-aspect.xml",
										"classpath:config/context-mybatis.xml",
										"classpath:config/context-transaction.xml" })
public class PurchaseServiceTest {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
//	@Test
	public void testAddPurchase() throws Exception{
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(new Product());
		purchase.getPurchaseProd().setProdNo(10008);
		purchase.setBuyer(new User());
		purchase.getBuyer().setUserId("user16");
		purchase.setPaymentOption("001");
		purchase.setReceiverName("홍길동");
		purchase.setReceiverPhone("010-1111-1111");
		purchase.setDlvyAddr("서울 은평구");
		purchase.setDlvyRequest("빠른 배송");
		purchase.setTranCode("002");
		purchase.setDlvyDate("2022-04-15");
		
		purchaseService.addPurchase(purchase);
		
		System.out.println(purchase);
		
		Assert.assertEquals("홍길동", purchase.getReceiverName());
	}
	
	@Test
	public void testGetPurchase() throws Exception{
		
		Purchase purchase = new Purchase();
		
		purchase = purchaseService.getPurchase(10000);
		
		System.out.println(purchase);
		
		Assert.assertEquals(10008, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user16", purchase.getBuyer().getUserId());
		Assert.assertEquals("홍길동", purchase.getReceiverName());
		Assert.assertEquals("001", purchase.getPaymentOption());
		
		Assert.assertNotNull(purchaseService.getPurchase(10000));
	}
	
//	@Test
	public void testUpdatePurchase() throws Exception{
		
		Purchase purchase = purchaseService.getPurchase(10001);
		Assert.assertNotNull(purchase);
		
		Assert.assertEquals(10009, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user16", purchase.getBuyer().getUserId());
		
		purchase.setPaymentOption("002");
		purchase.setReceiverName("이순신");
		purchase.setReceiverPhone("010-1234-1234");
		purchase.setDlvyAddr("서울시 강남구");
		purchase.setDlvyRequest("부재시 대문앞");
		purchase.setDlvyDate("2022-04-12");
		
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(10001);
		Assert.assertNotNull(purchase);
		
		//==> console 확인
		System.out.println(purchase);
		
		Assert.assertEquals(10009, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user16", purchase.getBuyer().getUserId());
		Assert.assertEquals("이순신", purchase.getReceiverName());
		Assert.assertEquals("002", purchase.getPaymentOption());
	}
	
//	@Test
	public void testUpdateTranCode() throws Exception{
		
		Purchase purchase = purchaseService.getPurchase(10001);
		Assert.assertNotNull(purchase);
		Assert.assertEquals("002", purchase.getTranCode());
		
		purchase.setTranCode("003");
		
		purchaseService.updateTranCode(purchase);
		
		purchase = purchaseService.getPurchase(10001);		
		Assert.assertNotNull(purchase);
		
		//==> console 확인
		System.out.println(purchase);
		Assert.assertEquals("003", purchase.getTranCode());		
	}
	
//	@Test
	public void testGetPurchaseListAll() throws Exception{
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("userId");
		search.setSearchKeyword("user16");
		Map<String, Object> map = purchaseService.getPurchaseList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(2, list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);		
	}
	
//	@Test
	public void testGetSaleListAll() throws Exception{
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		Map<String, Object> map = purchaseService.getPurchaseList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(2, list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);		
	}
}
