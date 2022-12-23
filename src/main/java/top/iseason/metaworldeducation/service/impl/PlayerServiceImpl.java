package top.iseason.metaworldeducation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.service.PlayerService;

@Service
@CacheConfig(cacheNames = "caffeineCacheManager")
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, PlayerInfo> implements PlayerService {
}
