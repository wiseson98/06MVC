package com.model2.mvc.web.product;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
public class ProductController {

	/// Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	/// Constructor
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
		   
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception{
		
		System.out.println("/addProductView.do");		
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product, Model model) throws Exception{
		
		System.out.println("/addProduct.do");
		
		productService.addProduct(product);
				
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		System.out.println("/getProduct.do");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		String cookieValue = null;
		Cookie cookie = null;
		
		cookieValue = Integer.toString(prodNo);
		
		if(request.getCookies() != null) {
			for(Cookie c : request.getCookies()) {
				if(c.getName().equals("history")) {
					cookieValue = URLDecoder.decode(c.getValue(), "euc-kr");
					cookieValue += "," + prodNo;
					System.out.println("CookieValue(history ????): " + cookieValue);
				}
			}
		}
		System.out.println("CookieValue: " + cookieValue);
		cookie = new Cookie("history", URLEncoder.encode(cookieValue, "euc-kr"));
		response.addCookie(cookie);
		
		return "forward:/product/readProduct.jsp";
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, @RequestParam(value = "menu", defaultValue = "search") String menu, Model model) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		return "forward:/product/listProduct.jsp";
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception{
		
		System.out.println("/updateProductView.do");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product ) throws Exception{
		
		System.out.println("/updateProduct.do");
		
		productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo=" + product.getProdNo();
	}	

}//end of class
