package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.config.JwtProvider;
import com.ozkan.bazaar.domain.AccountStatus;
import com.ozkan.bazaar.domain.USER_ROLE;
import com.ozkan.bazaar.exceptions.SellerException;
import com.ozkan.bazaar.model.Address;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.repository.IAddressRepository;
import com.ozkan.bazaar.repository.ISellerRepository;
import com.ozkan.bazaar.service.ISellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService implements ISellerService {

    private final ISellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAddressRepository addressRepository;


    @Override
    public Seller getSellerProfile(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {

        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());

        if(sellerExist!=null){
            throw new Exception("Seller already exists, use different email");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(seller.getPassword());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
       return sellerRepository.findById(id).orElseThrow(
                ()-> new SellerException("Seller not found with id:"+id)
        );

    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not found");
        }

        return seller;
    }

    @Override
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        Seller existingSeller = this.getSellerById(id);

        if(seller.getSellerName() != null){
            existingSeller.setSellerName(seller.getSellerName());
        }

        if(seller.getMobile() != null){
            existingSeller.setMobile(seller.getMobile());
        }

        if(seller.getEmail() != null){
            existingSeller.setEmail(seller.getEmail());
        }

        if(seller.getBusinessDetails() != null
            && seller.getBusinessDetails().getBusinessName() !=null ){
            existingSeller.getBusinessDetails().setBusinessName(
                    seller.getBusinessDetails().getBusinessName()
            );
        }

        if(seller.getBankDetails() != null
        && seller.getBankDetails().getAccountHolderName() != null
        && seller.getBankDetails().getAccountNumber() != null
        && seller.getBankDetails().getIfscCode() != null){

            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }

        if(seller.getPickupAddress() != null
        && seller.getPickupAddress().getAddress() != null
        && seller.getPickupAddress().getMobile() != null
        && seller.getPickupAddress().getCity() != null
        && seller.getPickupAddress().getState()!= null){

            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setZipCode(seller.getPickupAddress().getZipCode());
        }

        if(seller.getGSTIN() != null){
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {

        Seller seller = getSellerById(id);

        sellerRepository.delete(seller);

    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {

        Seller seller = getSellerByEmail(email);

        seller.setEmailVerified(true);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);

        seller.setAccountStatus(status);

        return sellerRepository.save(seller);
    }
}
