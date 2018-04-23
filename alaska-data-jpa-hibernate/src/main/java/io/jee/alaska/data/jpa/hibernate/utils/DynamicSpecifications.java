package io.jee.alaska.data.jpa.hibernate.utils;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import io.jee.alaska.util.Dates;

public class DynamicSpecifications {
	private static final ConversionService conversionService = new EnhanceDefaultConversionService();

	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> clazz) {
		return new Specification<T>() {
			private static final long serialVersionUID = -1523184233764045192L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!CollectionUtils.isEmpty(filters)) {

					List<Predicate> predicates = Lists.newArrayList();
					for (SearchFilter filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName,
						// 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						Class attributeClass = expression.getJavaType();
						if (!attributeClass.equals(String.class) && filter.value instanceof String
								&& conversionService.canConvert(String.class, attributeClass)) {
							// 对小于等于特殊处理.
							if (filter.operator == SearchFilter.Operator.LTE
									&& attributeClass.isAssignableFrom(Date.class)) {
								String oriValue = (String) filter.value;
								filter.value = Dates.parse(oriValue);
							}
							filter.value = conversionService.convert(filter.value, attributeClass);
						}

						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case NOTEQ:
							predicates.add(builder.notEqual(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case LLIKE:
							predicates.add(builder.like(expression, "%" + filter.value));
							break;
						case RLIKE:
							predicates.add(builder.like(expression, filter.value + "%"));
							break;
						case NLIKE:
							predicates.add(builder.notLike(expression, filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							if (filter.value instanceof Date && ((String) filter.originalValue).length() == Dates.CHINESE_DATE_FORMAT_LINE.length()) {
								Comparable v = Dates.addDay((Date) filter.value);
								predicates.add(builder.lessThan(expression, v));
							} else {
								predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							}
							break;
						case NULL:
							predicates.add(expression.isNull());
							break;
						case NOTNULL:
							predicates.add(expression.isNotNull());
							break;
						case IN:
							Predicate predicate = null;
							if (filter.value instanceof String) {
								String[] values = String.valueOf(filter.value).split(",");
								predicate = expression.in(Arrays.asList(values));
							} else if (filter.value instanceof String[]) {
								predicate = expression.in(Arrays.asList((String[]) filter.value));
							} else {
								predicate = expression.in((Collection<Object>) filter.value);
							}
							predicates.add(predicate);
							break;
						case NOTIN:
							Predicate predicate2 = null;
							if (filter.value instanceof String) {
								String[] values = String.valueOf(filter.value).split(",");
								predicate2 = expression.in(Arrays.asList(values));
							} else if (filter.value instanceof String[]) {
								predicate2 = expression.in(Arrays.asList((String[]) filter.value));
							} else {
								predicate2 = expression.in((Collection<Object>) filter.value);
							}
							predicates.add(builder.not(predicate2));
							break;
						}
					}

					// 将所有条件用 and 联合起来
					if (predicates.size() > 0) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}
}
