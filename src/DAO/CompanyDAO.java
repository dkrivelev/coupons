package DAO;

import java.util.Collection;

import Beans.Company;
import Beans.Coupon;
import Connection.CouponSystemException;

public interface CompanyDAO extends BasicDAO<Company> {
	
	Collection<Coupon> getCoupons(Company c) throws CouponSystemException;
	boolean login(String compName, String password) throws CouponSystemException;
	
}
