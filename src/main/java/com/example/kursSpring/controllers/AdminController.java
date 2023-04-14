package com.example.kursSpring.controllers;

import com.example.kursSpring.enumm.Status;
import com.example.kursSpring.models.Category;
import com.example.kursSpring.models.Image;
import com.example.kursSpring.models.Order;
import com.example.kursSpring.models.Product;
import com.example.kursSpring.repositories.CategoryRepository;
import com.example.kursSpring.repositories.OrderRepository;
import com.example.kursSpring.repositories.PersonRepository;
import com.example.kursSpring.security.PersonDetails;
import com.example.kursSpring.services.OrderService;
import com.example.kursSpring.services.PersonService;
import com.example.kursSpring.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {

    private final ProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PersonRepository personRepository;
    private final PersonService personService;

    public AdminController(ProductService productService, CategoryRepository categoryRepository, OrderRepository orderRepository, OrderService orderService, PersonRepository personRepository, PersonService personService) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("admin/product/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two") MultipartFile file_two, @RequestParam("file_three") MultipartFile file_three, @RequestParam("file_four") MultipartFile file_four, @RequestParam("file_five") MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        System.out.println(category_db.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        if (file_one != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if (file_two != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if (file_three != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if (file_four != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if (file_five != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }


    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "admin";
    }

    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id) {
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }

    @GetMapping("admin/orders")
    public String orderUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        return "ordersForAdmin";
    }

    @GetMapping("admin/order/edit/{id}")
    public String editOrder(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", orderService.getOrderId(id));
        model.addAttribute("status", Status.values());
        return "editOrder";
    }

    @PostMapping("admin/order/edit/{id}")
    public String editProduct(@ModelAttribute("order") Order order, @PathVariable("id") int id, Model model) {
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        Order changeOrder = orderService.getOrderId(id);
        changeOrder.setStatus(order.getStatus());
        orderService.updateOrder(id, changeOrder);
        return "redirect:/admin/orders";
    }

    @PostMapping("admin/order/search")
    public String orderSearch(@RequestParam("search") String search, Model model) {
        if (!search.isEmpty() && search.length() == 4) {
            model.addAttribute("orders", orderRepository.findByNumberEndingWith(search));
            model.addAttribute("value_search", search);
        } else {
            model.addAttribute("orders", orderRepository.findAll());
        }
        return "ordersForAdmin";
    }

    @GetMapping("admin/person")
    public String persons(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "person";
    }

    @GetMapping("admin/person/edit/{id}")
    public String editPersons(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.findById(id));
        return "editPerson";
    }

    @PostMapping("admin/person/setAdmin/{id}")
    public String editPersonsRoleAdmin(@PathVariable("id") int id, Model model) {
        personService.updatePersonRole(id, true);
        model.addAttribute("person", personService.findById(id));
        return "editPerson";
    }

    @PostMapping("admin/person/setUser/{id}")
    public String editPersonsRoleUser(@PathVariable("id") int id, Model model) {
        personService.updatePersonRole(id, false);
        model.addAttribute("person", personService.findById(id));
        return "editPerson";
    }

}
