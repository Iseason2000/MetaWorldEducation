package top.iseason.metaworldeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.metaworldeducation.config.MybatisCache;
import top.iseason.metaworldeducation.entity.UserFriendInfo;

@Mapper
@CacheNamespace(implementation = MybatisCache.class, eviction = MybatisCache.class)
public interface UserFriendInfoMapper extends BaseMapper<UserFriendInfo> {
}
