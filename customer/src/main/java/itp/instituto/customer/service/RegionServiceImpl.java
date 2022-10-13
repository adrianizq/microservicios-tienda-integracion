package itp.instituto.customer.service;



import itp.instituto.customer.entity.Region;

import itp.instituto.customer.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    RegionRepository regionRepository;

public List<Region> findAll(){
    return regionRepository.findAll();
}
    @Override
    public Region create(Region region) {
        Region regionVacia = new Region();
        regionVacia.setId((long) 0);
        regionVacia.setName("Region VAcia");

        Region regionDB = regionRepository.findById (region.getId ()).orElse(regionVacia);
        if (regionDB != null){
            return  regionDB;
        }


        regionDB = regionRepository.save ( region );
        return regionDB;
    }

    @Override
    public Region update(Region region) {
        Region regionDB = getRegion(region.getId());
        if (regionDB == null){
            return  null;
        }
        regionDB.setName(region.getName());


        return  regionRepository.save(regionDB);
    }

    @Override
    public void delete(Region region) {
        Region regionDB = getRegion(region.getId());
        if (regionDB !=null){
            regionRepository.delete(region);
        }

    }

    @Override
    public Region getRegion(Long id) {
        return  regionRepository.findById(id).orElse(null);
    }
}
