package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.ProductService;

import vn.hoidanit.laptopshop.domain.Product;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getDashboard(Model model) {
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("product", products);
        return "/admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductView(Model model) {
        model.addAttribute("newProduct", new Product());
        return "/admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(Model model, @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("productFile") MultipartFile file) {
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>" + error.getField() + "" + error.getDefaultMessage());
        }
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }
        String productImage = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(productImage);
        this.productService.handleSaveUser(product);
        return "redirect:/admin/product";
    }

    // delete product
    @GetMapping("/admin/product/delete/{id}")
    public String postDeleteProduct(@PathVariable long id) {
        this.productService.deleteProduct(id);
        return "redirect:/admin/product";
    }

    // update product

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.fetchProductById(id).get();
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @PathVariable long id,
            @ModelAttribute("newProduct") @Valid Product productUpdate,
            BindingResult newProductBindingResult, @RequestParam("productFile") MultipartFile file) {
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/update";
        }
        Product currentProduct = this.productService.fetchProductById(id).get();
        if (currentProduct != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }
            currentProduct.setName(productUpdate.getName());
            currentProduct.setPrice(productUpdate.getPrice());
            currentProduct.setDetailsDesc(productUpdate.getDetailsDesc());
            currentProduct.setShortDesc(productUpdate.getShortDesc());
            currentProduct.setQuantity(productUpdate.getQuantity());
            currentProduct.setFactory(productUpdate.getFactory());
            currentProduct.setTarget(productUpdate.getTarget());
            this.productService.handleSaveUser(currentProduct);
        }
        return "redirect:/admin/product";
    }

}
