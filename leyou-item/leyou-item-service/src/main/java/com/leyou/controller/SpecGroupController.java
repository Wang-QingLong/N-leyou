package com.leyou.controller;

import com.leyou.dto.SpecGroupDTO;
import com.leyou.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 15:49
 * @description:
 */
@RestController
@RequestMapping("/spec")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupService;

    /**
     * 根据categoryId查询规格组信息
     *
     * @param cid
     * @return
     */
    @GetMapping("/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> querySpecGroupsByCid(@RequestParam("id") Long cid) {


        return ResponseEntity.ok(specGroupService.querySpecGroupsByCid(cid));
    }




    /**
     * 查询规格参数组，及组内参数
     * @param id 商品分类id
     * @return 规格组及组内参数
     */
    @GetMapping("/of/category")
    public ResponseEntity<List<SpecGroupDTO>> querySpecsByCid(@RequestParam("id") Long id){
        return ResponseEntity.ok(specGroupService.querySpecsByCid(id));
    }



}
