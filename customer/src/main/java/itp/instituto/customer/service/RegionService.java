package itp.instituto.customer.service;


import itp.instituto.customer.entity.Region;

import java.util.List;

public interface RegionService {

    public List<Region> findAll();
    public Region create(Region region);
    public Region update(Region region);
    public void delete(Region region);
    public  Region getRegion(Long id);


}
