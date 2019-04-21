#include <graphenelib/system.h>
#include <graphenelib/asset.h>
#include <graphenelib/global.h>
#include <graphenelib/contract.hpp>
#include <graphenelib/dispatcher.hpp>
#include <graphenelib/multi_index.hpp>
#include <graphenelib/types.h>
#include <string>

using namespace graphene;
using std::string;
#define WAITING 0
#define RUNNING 1
#define EXIT 2


class play_2048 : public contract
{
  public:
    play_2048(uint64_t self)
        : contract(self)
        , multiplayers(_self, _self)
    {
    }





    //存储多人表
    /// @abi action
    void storemulti(uint64_t player_num ,uint64_t id, uint64_t state,uint64_t score)
    {
        
        multiplayers.emplace(_self, [&](auto &multiplayer) {  //使用_self让合约付钱,使用get_trx_sender()是调用者付钱
            multiplayer.primarykey=player_num;
            multiplayer.id = id;
            multiplayer.state = state;
            multiplayer.score = score;
        });
    }


    /********游戏操作********/

    /// @abi action
    void multilogin(uint64_t id){
        uint64_t player_num=1;
        for(auto it=multiplayers.begin(); it != multiplayers.end();++it){
            player_num++;  
        }
        storemulti(player_num,id,WAITING,0);
    }



    //多人表更新所有状态为RUNNING
    /// @abi action
    void setstaterun(uint64_t state)
    {
        for(auto it=multiplayers.begin(); it != multiplayers.end();++it){
            if(it->state==WAITING){
                multiplayers.modify(it, _self,[&state](auto &the_multiplayer){
                   the_multiplayer.state=RUNNING; 
                });
                print("multiplayer.id= ${id}", ("id", it->id));
            }
        }
        
    }

    //多人表更新状态为exit
    /// @abi action
    void setexit(uint64_t primarykey, uint64_t state){
        auto it = multiplayers.find(primarykey);
        if (it != multiplayers.end()) {
            multiplayers.modify(it, _self, [&state](auto &the_multiplayer) {
                the_multiplayer.state = state;
            });
        } 
    }

    //多人表更新分数
    /// @abi action
    void setmulsocre(uint64_t id,uint64_t score){
        uint64_t primarykey;
        for(auto it=multiplayers.begin(); it != multiplayers.end();++it){
            if (it->id==id && it->state==RUNNING)
            {
                primarykey=it->primarykey;
                multiplayers.modify(it, _self, [&score](auto &the_multiplayer) {
                    the_multiplayer.score = score;
                });
                setexit(primarykey,EXIT);
            }
        }
    }




   private:
    //@abi table multiplayer i64
    struct multiplayer {
        uint64_t primarykey;
        uint64_t id;
        uint64_t state;
        uint64_t score;

        uint64_t primary_key() const { return primarykey; }

        //string name;
        GRAPHENE_SERIALIZE(multiplayer, (primarykey)(id)(state)(score));
    };

    typedef graphene::multi_index<N(multiplayer), multiplayer> multiplayer_index;

    multiplayer_index multiplayers;

};
GRAPHENE_ABI(play_2048, (multilogin)(setmulsocre)(setstaterun))