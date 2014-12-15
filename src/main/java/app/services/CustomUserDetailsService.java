package app.services;

import app.domain.Admin;
import app.repositories.AdminRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsService")
@Transactional()
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private AdminRepository adminRepo;
    
    public CustomUserDetailsService() {
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Admin> admins = adminRepo.findByName(username);
        if (admins == null || admins.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username: "+ username);
        }
        
        Admin admin = admins.get(0);
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new org.springframework.security.core.userdetails.User(
            admin.getName(),
            admin.getPassword().toLowerCase(),
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            getAuthorities());
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authList = getGrantedAuthorities();
        return authList;
    }
    private List<GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        return authorities;
    }
}
