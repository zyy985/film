package org.gec.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.gec.pojo.Filminfo;
import org.gec.pojo.FilminfoExample;
import org.gec.pojo.Filmtype;
import org.gec.service.FilmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class FilmInfoController {

    @Autowired
    private FilmInfoService filmInfoService;

    public List<Filminfo> getResult() {
        return result;
    }

    //电影list
    List<Filminfo> result ;

    //每页显示数量
    public static final Integer PAGESIZE = 2;

    /*
         @RequestMapping("/findFilms")   == servlet 中的<url-pattern>/findFilms</url-pattern>
                                         == struts  <action name="findFilms" class="xxx"
     */


    @RequestMapping(value = "/findFilms" ,method = RequestMethod.POST)
    public ModelAndView findFilms(Filminfo film,FilminfoExample filminfo,@RequestParam(value = "typeid",required = false,defaultValue = "1") Integer typeid,
                                  @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum) throws Exception{

        FilminfoExample.Criteria criteria = filminfo.createCriteria();

        if (typeid != 0){
            criteria.andTypeidEqualTo(typeid);
        }
        //各种判断
        if ((StringUtils.isNotEmpty(film.getFilmname()))){
            criteria.andFilmnameLike("%"+film.getFilmname()+"%");
        }
        if ((StringUtils.isNotEmpty(film.getActor()))){
            criteria.andFilmnameLike("%"+film.getActor()+"%");
        }
        if ((StringUtils.isNotEmpty(film.getDirector()))){
            criteria.andFilmnameLike("%"+film.getDirector()+"%");
        }
        if(film.getBigprice() != null && film.getBigprice() != 0){
            criteria.andTicketpriceLessThan(new BigDecimal(film.getBigprice()));
        }

        if(film.getSmallprice() != null && film.getSmallprice() != 0){
            criteria.andTicketpriceGreaterThan(new BigDecimal(film.getSmallprice()));
        }


        //设置分页
        PageHelper.startPage(pageNum,PAGESIZE);

        //电影条件
        //查询
        result = filmInfoService.findAllInfo2(filminfo);

        ModelAndView mv = new ModelAndView();

        //分页上一页，下一页，尾页，总记录数...
        PageInfo info = new PageInfo(result);

        mv.addObject("info",info);
        mv.addObject("result",result);
        mv.addObject("typeid",typeid);
        mv.setViewName("page/result");
        return mv;
    }
    //检查用户名是否被占用
    @RequestMapping("/checkName")
    @ResponseBody
    public String checkName(String filmname){
        boolean result = filmInfoService.checkName(filmname);
        System.out.println("result:"+result);
        return result+"";
    }

    @RequestMapping("/FilmAddServlet")
    public String FilmAddServlet(Integer typeid, @Validated  Filminfo filminfo,
                                 BindingResult result, Model model,
                                 MultipartFile picname) throws Exception{

        //判断有没有较验错误
        if(result.hasErrors()){
            //循环获得错误
            List<FieldError> fieldErrors = result.getFieldErrors();
            for(FieldError error:fieldErrors){
                //errro.getField:出错的字段
                model.addAttribute(error.getField(),error.getDefaultMessage());//配置的错误信息
            }
            return "forward:/toAddFilm";
        }
        //电影类型
         Filmtype filmtype = new Filmtype();
         filmtype.setTypeid(typeid);

         //上传的文件名
        String fileName = picname.getOriginalFilename();
         //文件存储路径
        File file = new File("D:\\code\\file\\",fileName);

        filminfo.setPic(fileName);

        //写到磁盘
        picname.transferTo(file);

         //查询
         filmInfoService.save(filminfo);


        return "redirect:/toAddFilm";
    }

    //批量删除
    @RequestMapping("/deleteFilms")
    public String deleteFilms(int[] filmIds){
        //调用方法删除  request.getParameter("filmsIds")-->空指针
        filmInfoService.deleteByIds(filmIds);

        //重定向
        return "redirect:/toCinema";
    }

}
