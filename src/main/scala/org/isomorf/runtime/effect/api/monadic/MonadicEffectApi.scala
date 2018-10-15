package org.isomorf.runtime.effect.api.monadic

import org.isomorf.foundation.runtime.RTEffect

import org.isomorf.foundation.runtime.effects.plugins.EffectProvider

trait MonadicEffectApi extends EffectProvider {

  // START MONADIC
  def monadicInject[A](a: A): RTEffect[A]

  def monadicBind[A, B](m: RTEffect[A], f: A => RTEffect[B]): RTEffect[B]

  def monadicAndThen[A, B](ma: RTEffect[A], mb: RTEffect[B]): RTEffect[B]
  // END MONADIC

  def monadicZip[A, B](ma: RTEffect[A], mb: RTEffect[B]): RTEffect[(A, B)]
}
