package com.ecommerce.snapshop.service;


import com.ecommerce.snapshop.config.AppConstants;
import com.ecommerce.snapshop.exceptions.APIException;
import com.ecommerce.snapshop.exceptions.ResourceNotFoundException;
import com.ecommerce.snapshop.file.FileService;
import com.ecommerce.snapshop.model.Category;
import com.ecommerce.snapshop.model.Product;
import com.ecommerce.snapshop.payload.ProductDTORequest;
import com.ecommerce.snapshop.payload.ProductDTOResponse;
import com.ecommerce.snapshop.repository.CategoryRepository;
import com.ecommerce.snapshop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private FileService fileService;

    @Override
    public ProductDTORequest addProduct(ProductDTORequest productDTORequest, Long categoryId) {
        Product product=modelMapper.map(productDTORequest, Product.class);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Categorry Id",categoryId));
        product.setCategory(category);
        List<Product> products=category.getProducts();
        boolean ifProjectIsPresentOrNot=true;
        for(Product resultedProduct :products){
            if(resultedProduct.getName().equals(product.getName())){
                ifProjectIsPresentOrNot=false;
                break;
            }
        }
        if(ifProjectIsPresentOrNot){
            double specialPrice=product.getProductPrice()-(product.getProductPrice()*(productDTORequest.getDiscount()*0.01));
            product.setSpecialPrice(specialPrice);
            Product savedProduct=productRepository.save(product);
            return  modelMapper.map(savedProduct, ProductDTORequest.class);
        }else{
            throw new APIException("Product already exists");
        }
    }


    @Override
    public ProductDTOResponse getAllProduct(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortOrderBy);
        Page<Product> pageCategories=productRepository.findAll(pageDetails);
        List<Product> productList=pageCategories.getContent();
        if(productList.isEmpty()){
            throw new APIException("No Products found");

        }
        List<ProductDTORequest> productDTORequestList=productList.stream()
                .map(product -> modelMapper.map(product, ProductDTORequest.class)).toList();
        ProductDTOResponse productDTOResponse=new ProductDTOResponse();
        productDTOResponse.setProducts(productDTORequestList);
        productDTOResponse.setPageNumber(pageDetails.getPageNumber());
        productDTOResponse.setPageSize(pageDetails.getPageSize());
        productDTOResponse.setTotalElements(pageCategories.getTotalElements());
        productDTOResponse.setTotalPages(pageCategories.getTotalPages());
        productDTOResponse.setLastPage(pageCategories.isLast());
         return productDTOResponse;
    }

   @Override
   public ProductDTOResponse getProductById(Long categoryId, int pageNumber,
                                            int pageSize, String sortBy, String sortOrder) {
        Sort sortOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortOrderBy);
        Optional<Category> optionCategory=categoryRepository.findById(categoryId);
        if(!optionCategory.isPresent()){
            throw new ResourceNotFoundException("Category","Category Id",categoryId);
        }
        Page<Product> pageCategories= productRepository.findByCategory(optionCategory,pageDetails);
        List<Product> optionProduct=pageCategories.getContent();
        if(optionProduct.isEmpty()){
            throw new ResourceNotFoundException("Product","Product Id",optionCategory.get().getCategoryId());
        }
        List<ProductDTORequest> productDTORequestList=optionProduct.stream()
                .map(product -> modelMapper.map(product, ProductDTORequest.class)).toList();
        ProductDTOResponse productDTOResponse=new ProductDTOResponse();
        productDTOResponse.setProducts(productDTORequestList);
       productDTOResponse.setPageNumber(pageDetails.getPageNumber());
       productDTOResponse.setPageSize(pageDetails.getPageSize());
       productDTOResponse.setTotalElements(pageCategories.getTotalElements());
       productDTOResponse.setTotalPages(pageCategories.getTotalPages());
       productDTOResponse.setLastPage(pageCategories.isLast());
        return productDTOResponse;
    }

    @Override
    public ProductDTOResponse getProductBykeyWord(String keyword,int pageNumber, int pageSize,
                                                  String sortBy, String sortOrder) {
        Sort sortOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortOrderBy);
        Page<Product> pageCategories= productRepository.findByNameLikeIgnoreCase("%"+keyword+"%",pageDetails);
       List<Product> optionProduct=pageCategories.getContent();
        if(optionProduct.isEmpty()){
            throw new ResourceNotFoundException("Product","Keyword",keyword);
        }
        List<ProductDTORequest> productDTORequestList=optionProduct.stream()
                .map(product -> modelMapper.map(product, ProductDTORequest.class)).toList();
        ProductDTOResponse productDTOResponse=new ProductDTOResponse();
        productDTOResponse.setProducts(productDTORequestList);
        productDTOResponse.setPageNumber(pageDetails.getPageNumber());
        productDTOResponse.setPageSize(pageDetails.getPageSize());
        productDTOResponse.setTotalElements(pageCategories.getTotalElements());
        productDTOResponse.setTotalPages(pageCategories.getTotalPages());
        productDTOResponse.setLastPage(pageCategories.isLast());
        return productDTOResponse;
    }

    @Override
    public ProductDTORequest updateProductService(ProductDTORequest productDTORequest, Long productId) {
        Product product=modelMapper.map(productDTORequest, Product.class);
        Product savedProduct=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","Product Id",productId));
        if(product.getName()!=null){
            savedProduct.setName(product.getName());
        }
        if(product.getDescription()!=null){
            savedProduct.setDescription(product.getDescription());
        }

        savedProduct.setProductPrice(product.getProductPrice());
        savedProduct.setDiscount(product.getDiscount());
        if(product.getImage()!=null){
            savedProduct.setImage(product.getImage());
        }
        double specialPrice=product.getProductPrice()-(product.getProductPrice()*(productDTORequest.getDiscount()*0.01));
        savedProduct.setSpecialPrice(specialPrice);
        Product updateProductToDB=productRepository.save(savedProduct);
        return modelMapper.map(updateProductToDB, ProductDTORequest.class);
    }

    @Override
    public ProductDTORequest deleteProductService(Long productId) {
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","Product Id",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTORequest.class);
    }

    @Override
    public ProductDTORequest updateProductImageService(Long productId, MultipartFile image) throws IOException {
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","Product Id",productId));

        String path= AppConstants.IMAGE_PATH;
        String fileName=fileService.uploadImage(path,image);
        product.setImage(fileName);
        Product savedProductImage=productRepository.save(product);
        return modelMapper.map(savedProductImage, ProductDTORequest.class);
    }




}
