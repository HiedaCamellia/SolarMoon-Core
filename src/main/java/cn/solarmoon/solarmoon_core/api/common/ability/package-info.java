/**
 * Ability是应用于特定游戏事物的特定的能力，这种能力可以是任意的，但一定具有某种泛用性，如物品堆肥能力、简单添加可放置物品、方块实体的额外ticker等<br/>
 * 这些能力需要注册（或推入这些能力的集合中）才能使用，部分涉及到item或是实例的需要延迟注册
 */
package cn.solarmoon.solarmoon_core.api.common.ability;