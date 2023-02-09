package top.iseason.metaworldeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.metaworldeducation.entity.LabEquipment;


@Mapper
@CacheNamespace
public interface LabEquipmentMapper extends BaseMapper<LabEquipment> {
}

