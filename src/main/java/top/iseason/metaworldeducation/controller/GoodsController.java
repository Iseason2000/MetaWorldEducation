package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.iseason.metaworldeducation.entity.Goods;
import top.iseason.metaworldeducation.entity.GoodsRecord;
import top.iseason.metaworldeducation.entity.GoodsType;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.GoodsMapper;
import top.iseason.metaworldeducation.mapper.GoodsRecordMapper;
import top.iseason.metaworldeducation.mapper.GoodsTypeMapper;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "商品API，需登录")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    GoodsMapper goodsMapper;
    @Resource
    GoodsTypeMapper goodsTypeMapper;
    @Resource
    GoodsRecordMapper goodsRecordMapper;
    @Resource
    PlayerMapper playerMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "添加商品种类", notes = "需要 ADMIN 权限")
    @PostMapping("/type")
    public Result addType(
            @ApiParam("商品种类名称,限255字符") @RequestParam String name
    ) {
        GoodsType goodsType = new GoodsType();
        goodsType.setName(name);
        try {
            goodsTypeMapper.insert(goodsType);
        } catch (Exception e) {
            return Result.of(999, "种类已存在");
        }
        return Result.success(goodsType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "修改商品种类", notes = "需要 ADMIN 权限")
    @PutMapping("/type")
    public Result modifyType(
            @ApiParam("待修改的商品种类id") @RequestParam Integer id,
            @ApiParam("商品种类名称,限255字符") @RequestParam String name
    ) {
        GoodsType goodsType = goodsTypeMapper.selectById(id);
        if (goodsType == null) return Result.of(999, "商品类型不存在");
        goodsType.setName(name);
        goodsTypeMapper.updateById(goodsType);
        return Result.success(goodsType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "删除商品种类", notes = "同时会删除所有该类型的商品, 需要 ADMIN 权限")
    @DeleteMapping("/type")
    public Result removeType(
            @ApiParam("商品种类名id") @RequestParam Integer id
    ) {
        try {
            goodsTypeMapper.deleteById(id);
        } catch (Exception e) {
            return Result.of(999, "删除失败");
        }
        return Result.success();
    }

    @ApiOperation(value = "获取所有商品种类", notes = "需要 ADMIN 权限")
    @GetMapping("/type")
    public Result getTypes() {
        try {
            return Result.success(goodsTypeMapper.selectList(new LambdaQueryWrapper<>()));
        } catch (Exception e) {
            return Result.of(999, "删除失败");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @ApiOperation(value = "添加商品", notes = "需要 ADMIN 权限")
    @PostMapping("")
    public Result addGoods(
            @ApiParam("商品种类id") @RequestParam Integer goodsTypeId,
            @ApiParam("商品名称, 限255字符") @RequestParam String name,
            @ApiParam("商品价格") @RequestParam Double price,
            @ApiParam("商品投放数量") @RequestParam Integer amount,
            @ApiParam("商品库存数量") @RequestParam Integer stock
    ) {
        GoodsType goodsType = goodsTypeMapper.selectById(goodsTypeId);
        if (goodsType == null) return Result.of(999, "商品种类不存在");
        Goods goods = new Goods();
        goods.setName(name);
        goods.setPrice(price);
        goods.setAmount(amount);
        goods.setStock(stock);
        goodsMapper.insert(goods);
        return Result.success(goods);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @ApiOperation(value = "修改商品", notes = "需要 ADMIN 权限")
    @PutMapping("")
    public Result modifyGoods(
            @ApiParam("待修改的商品id") @RequestParam Integer id,
            @ApiParam("商品种类id") @RequestParam(required = false) Integer goodsTypeId,
            @ApiParam("商品名称, 限255字符") @RequestParam(required = false) String name,
            @ApiParam("商品价格") @RequestParam(required = false) Double price,
            @ApiParam("商品投放数量") @RequestParam(required = false) Integer amount,
            @ApiParam("商品库存数量") @RequestParam(required = false) Integer stock
    ) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) return Result.of(999, "商品不存在");
        GoodsType goodsType = goodsTypeMapper.selectById(goodsTypeId);
        if (goodsType == null) return Result.of(999, "商品种类不存在");
        if (name != null) goods.setName(name);
        if (price != null) goods.setPrice(price);
        if (amount != null) goods.setAmount(amount);
        if (stock != null) goods.setStock(stock);
        goodsMapper.updateById(goods);
        return Result.success(goods);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @ApiOperation(value = "删除商品", notes = "需要 ADMIN 权限")
    @DeleteMapping("")
    public Result removeGoods(
            @ApiParam("待删除的商品id") @RequestParam Integer id
    ) {
        int i = goodsMapper.deleteById(id);
        if (i == 1) Result.success();
        return Result.of(999, "商品不存在");
    }

    @ApiOperation(value = "获取某个商品的信息")
    @GetMapping("/")
    public Result getGoods(
            @ApiParam("商品id") @RequestParam(required = false) Integer id
    ) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) Result.of(999, "商品不存在");
        return Result.success(goods);
    }

    @ApiOperation(value = "获取商品列表")
    @GetMapping("/list")
    public Result getGoodsList(
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        List<Goods> goods = goodsMapper.selectList(
                new QueryWrapper<Goods>()
                        .last("limit " + page * count + "," + count));
        return Result.success(goods);
    }

    @Transactional
    @ApiOperation(value = "购买商品")
    @PostMapping("/buy")
    public Result removeGoods(
            @ApiIgnore Authentication authentication,
            @ApiParam("商品id") @RequestParam Integer id,
            @ApiParam("商品数量") @RequestParam Integer amount
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) Result.of(999, "商品不存在");
        if (goods.getAmount() < amount) {
            return Result.of(999, "商品数量不足");
        }
        goods.setAmount(goods.getAmount() - amount);
        GoodsRecord goodsRecord = new GoodsRecord();
        goodsRecord.setPlayerId(playerInfo.getPlayerId());
        goodsRecord.setGoodsId(id);
        goodsRecord.setAmount(amount);
        goodsRecord.setTime(new Date());
        goodsRecordMapper.insert(goodsRecord);
        return Result.of(999, "商品不存在");
    }

}
