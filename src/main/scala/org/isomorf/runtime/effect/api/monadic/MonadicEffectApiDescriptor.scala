package org.isomorf.runtime.effect.api.monadic

import org.isomorf.foundation.runtime.effects.plugins.EffectApiDescriptor

class MonadicEffectApiDescriptor extends EffectApiDescriptor[MonadicEffectApi] {

  override final val getHandle = "monadic"

  override final val getType = classOf[MonadicEffectApi]
}
