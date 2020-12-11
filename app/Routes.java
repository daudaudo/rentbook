package app;

import core.Facades.Router;
import core.Models.Models;
import core.Supports.SQL;

import java.util.ArrayList;
import java.util.HashMap;
import app.Models.Users;
import core.Facades.FormManager;

public class Routes {
    public static void register() {

        Router.belong().set("home", (req) -> {
            return () -> {
                FormManager.belong().setMainContentPane("main", "home");
            };
        });

        Router.belong().set("heart", (req) -> {
            return () -> {
                FormManager.belong().setMainContentPane("main", "heart");
            };
        }).middleware("auth");

        Router.belong().set("cart", (req) -> {

            return () -> {
                FormManager.belong().setMainContentPane("main", "cart");
            };
        }).middleware("auth");

        Router.belong().set("userinfo", (req) -> {
            return () -> {
                FormManager.belong().setMainContentPane("main", "userinfo");
            };
        }).middleware("auth");

        Router.belong().set("login", (req) -> {
            FormManager.belong().setMainContentPane("main", "login");
            return null;
        });

        Router.belong().set("login_post", (req) -> {
            ArrayList<Models> users = new Users(new String[] {
                SQL.where("username","=",req.input.get("user")),
                SQL.where("password","=",req.input.get("pass")),
            }).getModels();
            
            if(users.isEmpty()) return ()->{
                FormManager.belong().showAlert("Sai tài khoản hoặc mật khẩu");
            };

            else{
                req.session().add("user", req.input.get("user"));
                req.session().add("pass", req.input.get("pass"));

                return ()->{
                    Router.belong().route("home");
                };
            }
        });

        Router.belong().set("signin",(req)->{
            FormManager.belong().setMainContentPane("main", "signin");
            return null;
        });

        Router.belong().set("signin_post", (req)->{

            req.session().add("user", req.input.get("user"));
            req.session().add("pass", req.input.get("pass"));

            HashMap<String,Object> user = new HashMap<>();
            user.put("username",req.input.get("user"));
            user.put("password",req.input.get("pass"));
            user.put("email",req.input.get("email"));
            
            new Users(user).insert();

            req.update();

            return ()->{
                Router.belong().route("home");
            };
        });

        Router.belong().set("logout",(req)->{
            req.session().clear();
            req.update();
            return ()->{
                Router.belong().route("home");
            };
        });

        Router.belong().set("search",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "search");
            };
        });

        Router.belong().set("category",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "search");
            };
        });
        
        Router.belong().set("book",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "book");
            };
        });
        

        Router.belong().set("address",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "address");
            };
        });
    }
}
